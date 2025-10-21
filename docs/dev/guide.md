# 🛠️ Developer Guide

## 📋 Yêu cầu

* **[Oracle OpenJDK 24](https://openjdk.org/projects/jdk/24/)** (Language level: 24 Preview)
* **[Gradle](https://gradle.org/)**

## 🚀 Setup

1. **Clone repository:**

```bash
git clone https://github.com/CodeStormOOP/bounceverse.git
cd bounceverse
```

2. **Build chương trình:**

```bash
./gradlew
```

## 🧹 Linter

* Kiểm tra code convention:

```bash
./gradlew spotlessCheck
```

* Tự động sửa lỗi convention:

```bash
./gradlew spotlessApply
```

* Tạo Git pre-push hook để tự động kiểm tra trước khi push:

```bash
./gradlew spotlessInstallGitPrePushHook
```

Chi tiết: [Spotless Gradle Plugin](https://github.com/diffplug/spotless/tree/main/plugin-gradle)

## 📏 Conventions

* **Code:** [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* **Commits:** [Conventional Commits](https://www.conventionalcommits.org/) (và viết message bằng **Tiếng Anh**)
* **Files Structure:**
  [Gradle project Structure](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html)
