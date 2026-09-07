# 블루그린 무중단 배포

`main` 푸시 시 `.github/workflows/deploy.yml`이 실행된다. 컨테이너를 교체하는 동안 트래픽이 끊기지 않는다.

## 동작

```
현재 활성 포트를 nginx upstream 파일에서 읽음
  → 반대 포트(8081 ↔ 8082)로 신규 컨테이너 기동
  → 헬스체크 폴링 (최대 240초)
      성공: upstream 파일 교체 → nginx -t → reload → 구 컨테이너 제거
      실패: 신규 컨테이너만 제거, 트래픽은 기존 포트 유지 (배포 실패 처리)
```

nginx `reload`는 기존 커넥션을 유지한 채 워커를 교체하므로 전환 순간에도 요청이 끊기지 않는다.

## 서버 사전 구성 (VM 재구축 시 필요)

1. upstream 파일 생성

```bash
sudo tee /etc/nginx/conf.d/trash-heroes-upstream.conf > /dev/null <<'CONF'
upstream trash_heroes_backend {
    server 127.0.0.1:8081;
}
CONF
```

2. `/etc/nginx/sites-available/default`의 `proxy_pass`를 upstream 참조로 변경

```nginx
location / {
    proxy_pass http://trash_heroes_backend;   # 기존: http://127.0.0.1:8080
    ...
}
```

3. `sudo nginx -t && sudo nginx -s reload`

> nginx 설정 백업은 `/etc/nginx/sites-enabled/` **밖**에 둘 것. 해당 디렉터리는 전부 로드되므로 백업 파일이 중복 server 블록으로 파싱돼 `nginx -t`가 실패한다.

## 제약

VM 사양이 2코어 / RAM 954Mi라 전환 구간에 컨테이너 2개가 동시에 뜨면 물리 메모리를 약 120Mi 초과한다. 스왑(2Gi)으로 흡수되며 OOM은 발생하지 않지만, 전환 중 구 컨테이너 일부가 스왑으로 밀려 응답이 느려질 수 있다.

이를 제한하려고 두 컨테이너 모두 `-Xmx240m`(기존 ergonomic 값과 동일)과 `--memory=520m`을 명시한다. 한쪽이 부풀어 다른 쪽을 밀어내는 상황을 막기 위함이다.

애플리케이션 기동에 100초 이상 걸리므로(메모리 경합 시 160초 관측) 헬스체크 타임아웃은 240초로 잡았다.

## 헬스체크

현재는 `/favicon.ico`의 HTTP 응답 수신 여부로 판정한다(5xx 미만이면 통과). 애플리케이션이 요청을 처리할 수 있는 상태인지까지만 확인하며, DB 연결은 검증하지 않는다.

`spring-boot-starter-actuator`를 추가하고 `SecurityConstants.ALLOWED`에 `/actuator/health`를 넣으면 DB 연결까지 확인하는 readiness 체크로 올릴 수 있다.

## 포트

- `8081` blue / `8082` green — 둘 다 `127.0.0.1`에만 바인딩되어 외부에서 직접 접근할 수 없다.
- 기존에는 `0.0.0.0:8080`으로 열려 있어 TLS를 우회한 평문 접근이 가능했다.
