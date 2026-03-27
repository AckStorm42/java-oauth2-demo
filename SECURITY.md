# Security Policy

## Overview

This repository follows secure-by-default principles where possible, even as a minimal demo.

It is **not intended for production use** and does not implement all security controls required for a production-grade OAuth client.

---

## Supported Versions

This project is a lightweight demo without formal versioning.
Security updates, when applicable, are made on the default branch.

---

## Reporting a Vulnerability

If you discover a security issue in this repository, please do **not** open a public issue with sensitive details.

Instead, report it privately by:

- Opening a GitHub security advisory (if enabled), or
- Contacting the repository owner directly

When reporting a vulnerability, please include:

- A clear description of the issue
- Steps to reproduce (if applicable)
- Potential impact
- Any suggested remediation

---

## Scope and Expectations

This project intentionally focuses on clarity and minimalism. As such, it includes limitations that would need to be addressed in real-world systems, including:

- No integration with a secrets manager (environment variables are used instead)
- No token caching or refresh handling
- No retry or backoff logic for HTTP calls
- No TLS certificate pinning or advanced transport security configuration
- No JWT validation (this demo acts as an OAuth client, not a resource server)

These trade-offs are intentional to keep the example easy to understand.

---

## Security Practices in This Repository

Despite being a demo, the following practices are applied:

- No hard-coded secrets in source code
- Configuration via environment variables
- Fail-fast validation for required configuration
- Avoidance of logging sensitive data (e.g., access tokens)
- Minimal dependency surface

---

## Responsible Use

This code is provided for educational purposes. If you adapt it for real systems:

- Use a secure secrets management solution
- Enforce proper TLS validation and configuration
- Implement token lifecycle management
- Apply appropriate logging and monitoring controls
- Conduct a full security review before deployment

---

## Disclosure Policy

Security issues will be addressed in a timely manner once reported and validated.
Fixes may be applied directly to the main branch without formal release tagging.

---

## Acknowledgements

Thanks to the security community and developers who responsibly disclose issues and help improve software quality.
