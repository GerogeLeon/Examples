- POST

```
curl -d '{"id":"1","text":"hello"}' -H "Content-Type: application/json" -X POST http://localhost:8080/tweets

```

- GET
```
curl http://localhost:8080/tweets/1
```