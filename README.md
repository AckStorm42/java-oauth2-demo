# java-oauth2-demo

A minimal Java 21 OAuth 2.0 client credentials demo for local learning and experimentation.

This project shows how to:

- request an access token from an OAuth 2.0 token endpoint
- optionally call a protected API using that token
- handle configuration securely using environment variables

---

## 🎯 Purpose

This repository is intentionally small and easy to understand.

It is designed for:

- learning OAuth 2.0 client credentials flow
- testing against a local Keycloak lab
- demonstrating secure configuration patterns in plain Java
- serving as a lightweight, interview-friendly example

---

## ⚠️ Security Notice

This project is for **local development and demo purposes only**.

It is **not production-ready**. For example:

- no secret manager integration (env vars only)
- no token caching or refresh handling
- no retry/backoff logic
- no TLS pinning or advanced validation
- assumes a trusted local/test identity provider

🚫 Do not commit real secrets
🔐 Always rotate credentials if exposed

---

## 🧰 Requirements

- OpenJDK 21
- Maven 3.9+
- OAuth 2.0 provider (e.g., Keycloak)

---

## 🔐 Environment Variables

Set the following before running:

| Variable             | Required | Description            |
| -------------------- | -------- | ---------------------- |
| `TOKEN_URL`          | ✅       | OAuth token endpoint   |
| `CLIENT_ID`          | ✅       | OAuth client ID        |
| `CLIENT_SECRET`      | ✅       | OAuth client secret    |
| `SCOPE`              | ❌       | Optional scope         |
| `API_URL`            | ❌       | Protected API endpoint |
| `CALL_PROTECTED_API` | ❌       | `true` to call API     |

---

## 🧪 Example (Keycloak Lab)

```bash
export TOKEN_URL="http://localhost:8081/realms/lab/protocol/openid-connect/token"
export CLIENT_ID="spring-demo"
export CLIENT_SECRET="your-client-secret"
export CALL_PROTECTED_API="false"
```

If your client requires scope:

```bash
export SCOPE="read:demo"
```

---

## ⚙️ Build

```bash
mvn clean package
```

---

## ▶️ Run

```bash
mvn clean compile exec:java
```

---

## 🚀 Run (with environment variables)

```bash
export TOKEN_URL="http://localhost:8081/realms/lab/protocol/openid-connect/token"
export CLIENT_ID="spring-demo"
export CLIENT_SECRET="your-client-secret"

mvn clean compile exec:java
```

---

## ✅ Expected Output

```text
Access token acquired successfully.
Token type: Bearer
Expires in: 300 seconds
```

---

## 🔗 Optional: Call Protected API

```bash
export API_URL="https://your-api.example.com/protected"
export CALL_PROTECTED_API="true"

mvn clean compile exec:java
```

---

## 🧩 Design Choices

This demo intentionally prioritizes clarity and security fundamentals over completeness:

- Uses environment variables instead of hard-coded configuration
- Fails fast when required configuration is missing
- Avoids logging sensitive data such as access tokens
- Keeps dependencies minimal to reduce attack surface
- Uses Java 21 standard libraries for HTTP communication

These choices reflect secure-by-default patterns commonly used in production systems.

## 🧠 Key Concepts Demonstrated

- OAuth 2.0 Client Credentials Flow
- Secure configuration via environment variables
- Minimal HTTP client usage (Java 21 `HttpClient`)
- Safe handling of sensitive data (no token logging)
- Fail-fast configuration validation

---

## 🔬 Keycloak Notes

This works well with a local Keycloak setup:

- Realm: `lab`
- Confidential client
- Client credentials flow enabled

Token endpoint example:

```
http://localhost:8081/realms/lab/protocol/openid-connect/token
```

---

## 🛡️ Security Practices in This Repo

- ✅ No hardcoded secrets
- ✅ Environment-based configuration
- ✅ No access token leakage in logs
- ✅ Minimal dependency surface
- ✅ Clear separation of demo vs production concerns

---

## 📄 License

MIT License

---

## 🙌 Why This Exists

This project is part of a broader effort to demonstrate practical, developer-friendly application security patterns—especially around authentication, secure configuration, and OAuth 2.0 flows.

---

## 💬 Feedback

Feel free to open an issue or reach out with suggestions or improvements.
