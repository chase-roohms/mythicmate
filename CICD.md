# GitHub Actions CI/CD Workflows

This repository includes three automated GitHub Actions workflows for building, versioning, and publishing Docker images.

## Prerequisites

### 1. Docker Hub Account Setup

1. Create a [Docker Hub](https://hub.docker.com/) account if you don't have one
2. Create a new repository named `mythicmate` at https://hub.docker.com/repository/create
3. Generate an access token:
   - Go to Account Settings → Security → [New Access Token](https://hub.docker.com/settings/security)
   - Name it "GitHub Actions" 
   - Set permissions to "Read, Write, Delete"
   - **Copy the token immediately** (you can't see it again)

### 2. GitHub Secrets Setup

Add these secrets to your GitHub repository at `Settings → Secrets and variables → Actions → New repository secret`:

| Secret Name | Description | Example |
|------------|-------------|---------|
| `DOCKERHUB_USERNAME` | Your Docker Hub username | `chase-roohms` |
| `DOCKERHUB_TOKEN` | Your Docker Hub access token | `dckr_pat_xxxxxxxxxxxxx` |

## Workflows Overview

### 1. 📦 `docker-publish.yml` - Main Branch Auto-Build

**Trigger:** Automatically runs on every push to `main` branch

**What it does:**
- Builds Docker image for both `linux/amd64` and `linux/arm64` platforms
- Pushes image to Docker Hub with `:main` tag
- Runs Docker Scout security scan
- Uploads vulnerability report to GitHub Security tab

**Docker Hub Tag:**
```
username/mythicmate:main
```

**Usage:** This is your "latest development" image. Every commit to main automatically updates this tag.

---

### 2. 🏷️ `version-bump.yml` - Manual Version Release

**Trigger:** Manually triggered via GitHub Actions UI

**What it does:**
1. Gets the current version from git tags (or starts at v0.0.0)
2. Bumps version based on your selection (major/minor/patch)
3. Creates and pushes new git tag
4. Creates GitHub Release with auto-generated release notes
5. Triggers docker-release workflow to build versioned images

**How to use:**

1. Go to Actions tab in GitHub
2. Click "Version Bump and Release" workflow
3. Click "Run workflow"
4. Select bump type:
   - **patch** (v1.0.0 → v1.0.1) - Bug fixes, small changes
   - **minor** (v1.0.0 → v1.1.0) - New features, backwards compatible
   - **major** (v1.0.0 → v2.0.0) - Breaking changes

**Example:**
```bash
Current version: v1.2.3

Select "patch"  → Creates v1.2.4
Select "minor"  → Creates v1.3.0
Select "major"  → Creates v2.0.0
```

---

### 3. 🚀 `docker-release.yml` - Version Build & Push

**Trigger:** Called by `version-bump.yml` (not run directly)

**What it does:**
- Builds Docker image for both platforms
- Pushes multiple tags to Docker Hub:
  - `:latest` - Always points to newest stable version
  - `:vX.Y.Z` - Full version (e.g., `:v1.2.3`)
  - `:vX.Y` - Minor version (e.g., `:v1.2`)
  - `:vX` - Major version (e.g., `:v1`)

**Example output for v1.2.3:**
```
username/mythicmate:latest
username/mythicmate:v1.2.3
username/mythicmate:v1.2
username/mythicmate:v1
```

**Why multiple tags?**
- Users can pin to exact versions: `mythicmate:v1.2.3`
- Or get latest patch fixes: `mythicmate:v1.2`
- Or stay on major version: `mythicmate:v1`
- Or always get newest: `mythicmate:latest`

---

## Common Workflows

### First Time Setup

```bash
# 1. Set up secrets in GitHub (see Prerequisites above)

# 2. Push to main to test auto-build
git add .
git commit -m "Add Docker workflows"
git push origin main

# 3. Watch Actions tab - should see "Build and Push Docker Image" running

# 4. Create first release
# Go to Actions → Version Bump and Release → Run workflow → Select "patch"
# This will create v0.0.1 (or v1.0.0 if you prefer to start there)
```

### Regular Development

```bash
# Work on features
git add .
git commit -m "Add new dice rolling feature"
git push origin main
# → Automatically builds and pushes :main tag
```

### Creating a Release

**For bug fixes:**
```bash
# Go to GitHub Actions → Version Bump and Release → patch
# v1.2.3 → v1.2.4
```

**For new features:**
```bash
# Go to GitHub Actions → Version Bump and Release → minor
# v1.2.3 → v1.3.0
```

**For breaking changes:**
```bash
# Go to GitHub Actions → Version Bump and Release → major
# v1.2.3 → v2.0.0
```

---

## Deployment

### Using the Images

**Latest stable release:**
```bash
docker pull username/mythicmate:latest
```

**Specific version:**
```bash
docker pull username/mythicmate:v1.2.3
```

**Latest development (main branch):**
```bash
docker pull username/mythicmate:main
```

### Update docker-compose.yml to use published images

Instead of building locally, you can use published images:

```yaml
version: '3.8'

services:
  mythicmate:
    image: YOUR_DOCKERHUB_USERNAME/mythicmate:latest
    # Or use a specific version:
    # image: YOUR_DOCKERHUB_USERNAME/mythicmate:v1.0.0
    container_name: mythicmate-bot
    restart: unless-stopped
    environment:
      - MYTHICMATE_TOKEN=${MYTHICMATE_TOKEN}
      - MYTHICMATE_PASSWORD=${MYTHICMATE_PASSWORD}
      - MYTHICMATE_GPT_KEY=${MYTHICMATE_GPT_KEY}
    volumes:
      - ./database:/app/database
```

Then simply:
```bash
docker-compose pull  # Get latest image
docker-compose up -d  # Start/restart
```

---

## Security Features

### Docker Scout Integration

The `docker-publish.yml` workflow includes Docker Scout scanning:
- Checks for CVEs (security vulnerabilities)
- Reports findings in GitHub Security tab
- Results appear under "Security → Code scanning alerts"

### View Security Reports

1. Go to your repo's "Security" tab
2. Click "Code scanning alerts"
3. Filter by "Docker Scout"

---

## Troubleshooting

### Workflow Fails - Authentication Error

**Error:** `denied: requested access to the resource is denied`

**Fix:**
1. Verify `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` secrets are set correctly
2. Check token hasn't expired
3. Verify Docker Hub repository exists: `https://hub.docker.com/r/YOUR_USERNAME/mythicmate`

### Workflow Fails - Build Error

**Check the logs:**
1. Go to Actions tab
2. Click the failed workflow
3. Expand the failed step
4. Look for build errors (usually Java/Maven related)

**Common issues:**
- Missing dependencies in pom.xml
- Java version mismatch
- Test failures (builds with `-DskipTests` so shouldn't happen)

### Docker Scout Fails

This is non-critical - if Docker Scout fails, the image still builds and pushes. Scout just provides security scanning.

### Version Already Exists

**Error:** `tag already exists`

**Cause:** You tried to create a version that already exists

**Fix:** 
- Delete the tag locally and on GitHub, then try again
- Or just create the next version

```bash
# Delete tag locally and remotely
git tag -d v1.0.0
git push --delete origin v1.0.0

# Then re-run version bump workflow
```

---

## Advanced Configuration

### Change Version Starting Point

If you want to start at v1.0.0 instead of v0.0.0:

```bash
# Manually create first tag
git tag -a v1.0.0 -m "Initial release v1.0.0"
git push origin v1.0.0

# Then use version bump workflow for future releases
```

### Only Build for One Platform

Edit the workflows and change:
```yaml
platforms: linux/amd64,linux/arm64
```
to:
```yaml
platforms: linux/amd64
```

This will speed up builds but won't work on ARM devices (like Raspberry Pi or Apple Silicon Macs).

### Skip Security Scanning

Remove or comment out the "Docker Scout Scan" and "Upload SARIF result" steps in `docker-publish.yml` if you don't need them.

---

## Cost Considerations

**GitHub Actions:**
- Free for public repositories
- 2,000 minutes/month for private repos on free plan
- Each build takes ~5-10 minutes

**Docker Hub:**
- Free tier: Unlimited public repositories
- Rate limiting: 100 pulls per 6 hours for anonymous users
- Unlimited pulls for authenticated users

**Recommendations:**
- Keep repository public for unlimited Actions minutes
- Log in to Docker Hub when pulling images to avoid rate limits:
  ```bash
  docker login
  ```

---

## FAQ

**Q: Can I trigger releases automatically based on commits?**  
A: Yes! You can modify `version-bump.yml` to trigger on push events and parse commit messages for version bumps (e.g., `[major]`, `[minor]`, `[patch]` in commit message).

**Q: Why does main branch build separately from releases?**  
A: The `:main` tag serves as a "nightly build" for testing latest changes. Releases (`:latest`, `:v1.0.0`) are stable versions.

**Q: Can I publish to GitHub Container Registry instead?**  
A: Yes! Change the registry URLs and update secrets to use `GITHUB_TOKEN` instead of Docker Hub credentials.

**Q: How do I roll back a release?**  
A: Just update your docker-compose.yml to use an older version tag:
```yaml
image: username/mythicmate:v1.0.0  # Specific old version
```

---

## Next Steps

1. ✅ Set up Docker Hub and GitHub secrets
2. ✅ Push to main to test auto-build
3. ✅ Create your first release using version bump workflow
4. ✅ Update deployment documentation with image URLs
5. ✅ Consider setting up automated deployments on your server

---

For more information, see:
- [Docker.md](DOCKER.md) - Docker setup and deployment guide
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Hub Documentation](https://docs.docker.com/docker-hub/)
