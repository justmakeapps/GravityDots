can you genera# Gravity Dots — Google Play Release Guide

Everything you need to publish **Gravity Dots** to the Google Play Store: build steps, store listing copy, image specs, screenshot capture checklist, Data Safety / Content Rating answers, and the submission workflow.

- **Package name:** `com.justmakeapps.gravitydots`
- **Version:** 1.0 (versionCode 1)
- **Min / Target SDK:** 24 / 34
- **Category:** Games → Puzzle
- **Content rating:** Everyone (PEGI 3 / ESRB E)
- **Contains ads:** Yes (AdMob banner + rewarded)
- **In-app purchases:** No

---

## 1. Pre-release build checklist

- [ ] `versionCode` and `versionName` bumped in `app/build.gradle.kts` if re-uploading
- [ ] Real AdMob App ID present in `AndroidManifest.xml` (currently: `ca-app-pub-3307436672128190~6134186160`)
- [ ] Real AdMob ad-unit IDs in `ads/AdManager.kt` (not the Google test IDs)
- [x] `isMinifyEnabled = true` + `isShrinkResources = true` enabled; AdMob ProGuard rules in `app/proguard-rules.pro`
- [ ] Run on a clean device — verify banner loads, rewarded ad plays, +2 / +3 walls and level unlock all work
- [ ] No crashes on rotate, background/foreground, or repeated level restarts
- [ ] App icon renders correctly on all densities (mdpi → xxxhdpi)

### Generate a signed release bundle (AAB)

Play Console requires an **.aab**, not an APK.

1. Android Studio → **Build → Generate Signed Bundle / APK… → Android App Bundle**
2. Create (or reuse) a keystore. **Back this file up** — losing it means you can never update the app again.
   - Recommended path: outside the repo (e.g. `~/keystores/gravitydots.jks`)
3. Build variant: `release`
4. Output: `app/release/app-release.aab` — this is what you upload.

> First upload only: enable **Play App Signing** in the Console. Google will manage the upload key going forward; keep your local keystore safe regardless.

---

## 2. Store listing — copy/paste ready

### App name (max 30 chars)
```
Gravity Dots
```

### Short description (max 80 chars)
```
Draw walls, guide the dots, match the colors. A calm physics puzzle.
```

### Full description (max 4000 chars)
```
Gravity Dots is a minimal physics puzzle where you guide colored dots into matching holes by drawing walls on the screen.

No timers. No combos. No noise. Just gravity, a finger, and a level to solve.

— HOW TO PLAY —
• Tap and drag to draw a wall
• Dots fall under gravity and bounce off your walls
• Get every dot into its matching colored hole
• You have a limited number of walls per level — use them wisely

— FEATURES —
• 8 handcrafted levels, more on the way
• Clean, minimal art — easy on the eyes
• One-handed portrait play
• Works offline
• No account, no signup, no tracking beyond ads

— ADS —
Ads are never forced. A small banner sits at the bottom of the screen. Optional rewarded ads let you:
• Get +2 walls when you run out mid-level
• Earn +3 bonus walls for any future level
• Unlock extra levels from the level select screen

You always choose whether to watch.

— WHO IT'S FOR —
If you like Threes, Mini Metro, or any puzzle that respects your time, Gravity Dots is built for you. Play one level on a coffee break, or chain ten in a sitting.

Made by Just Make Apps. Feedback welcome — every review is read.
```

### App category
- **Type:** Game
- **Category:** Puzzle
- **Tags (optional, pick up to 5):** Brain Games, Casual, Physics, Minimalist, Offline

### Contact details
- **Email:** _your-support-email@domain.com_
- **Website:** _optional, but recommended_
- **Phone:** _leave blank unless required for your country_

### Privacy policy URL
**Required** because the app shows ads. Host a simple page at e.g. `https://justmakeapps.com/gravitydots/privacy`. A minimal template is in **Section 6** below.

---

## 3. Graphic assets — required by Play Console

You **must** upload these. Specs are exact — Play Console will reject anything off-size.

| Asset | Required | Format | Size | Notes |
|---|---|---|---|---|
| **App icon** | ✅ | PNG (32-bit, alpha) | **512 × 512** | Use the same artwork as `ic_launcher` but full-resolution. No rounded corners — Play crops automatically. |
| **Feature graphic** | ✅ | PNG or JPG | **1024 × 500** | The banner at the top of your store page. No transparency. Keep text minimal — much of it gets cropped on small screens. |
| **Phone screenshots** | ✅ (min 2, max 8) | PNG or JPG | **1080 × 1920** (or 1080 × 2400) portrait | Show real gameplay. See capture checklist below. |
| **7-inch tablet screenshots** | Optional | PNG/JPG | 1200 × 1920 | Skip unless you plan to market on tablets. |
| **10-inch tablet screenshots** | Optional | PNG/JPG | 1920 × 1200 or 2560 × 1600 | Skip for now. |
| **Promo video (YouTube URL)** | Optional | — | — | 30–60s gameplay clip. Big conversion boost if you have one. |

### Suggested screenshot set (8 frames)

Capture these in-game on a 1080×1920+ device (or emulator) using **adb screencap** or the screenshot button. Order matters — frame 1 is the hero.

1. **Mid-level gameplay** — dots falling, a couple of walls drawn, the goal visible. Most compelling frame; goes first.
2. **Level select screen** — shows progression / unlocked levels.
3. **A harder level with several walls drawn** — communicates depth.
4. **Level complete dialog** — feel-good moment.
5. **Out-of-walls dialog** — shows the optional rewarded ad ("Watch for +2 walls") so reviewers see ad behavior is user-initiated.
6. **A different color combination** — visual variety.
7. **Home screen** — branding shot.
8. **Settings screen** — optional, only if it looks tidy.

### How to capture clean screenshots

```bash
# With the emulator or a USB-connected device running the app:
adb shell screencap -p /sdcard/screen.png && adb pull /sdcard/screen.png ./screenshot-1.png
```

Or use Android Studio: **View → Tool Windows → Logcat → camera icon** captures the current device frame.

Tips:
- Disable system UI gestures bar overlay if it intrudes (Developer Options → Hide overlays).
- Take 12–15 shots, pick the best 8.
- Don't add marketing frames/borders for v1 — raw gameplay converts better and is faster to ship.

### Feature graphic — quickest path

You don't need a designer. Open Figma/Canva, 1024×500, dark background that matches the app's `Theme.GravityDots`, drop 3–4 colored dots and one curved white wall stroke, and the words **"Gravity Dots"** in the lower-left. Export PNG. Done.

---

## 4. Data Safety form — answers

In Play Console → **App content → Data safety**, you'll fill a questionnaire. Based on the current code (only AdMob + INTERNET / ACCESS_NETWORK_STATE permissions, no analytics SDKs):

| Question | Answer |
|---|---|
| Does your app collect or share any of the required user data types? | **Yes** (AdMob collects advertising ID + approximate device info) |
| Is all of the user data collected by your app encrypted in transit? | **Yes** |
| Do you provide a way for users to request that their data be deleted? | **Yes** — via the email in your privacy policy |

**Data types — declare these (collected & shared by AdMob):**
- **Device or other IDs** — purpose: Advertising or marketing. Collected ✅ Shared ✅. Not optional (ad SDK requires it).
- **App activity → App interactions** — purpose: Analytics, Advertising. Optional ✅ if you implement a consent prompt (you don't yet, so mark as required).
- **Approximate location** — only if AdMob region-targets; mark **Collected, not shared, required** to be safe.

**Do NOT declare** (the app doesn't touch these):
- Personal info, financial info, photos/videos, audio, contacts, calendar, messages, files, health/fitness, web history, calls, body sensors.

> If you later add Google Analytics, Firebase, or a consent banner, revisit this form — it's the most common reason for re-review delays.

---

## 5. Content rating questionnaire

Play Console → **App content → Content ratings**. You'll get IARC ratings (ESRB / PEGI / etc.) from a short questionnaire. For Gravity Dots, answer **No** to every question — no violence, no profanity, no gambling, no user-to-user communication, no location sharing, no purchases.

Result will be:
- **ESRB:** Everyone
- **PEGI:** 3
- **USK:** 0
- **IARC generic:** 3+

---

## 6. Privacy policy — minimal template

Host the following at a public URL (GitHub Pages, your domain, anywhere reachable). Paste this verbatim and replace the two placeholders.

```
Privacy Policy — Gravity Dots

Last updated: [DATE]

Gravity Dots ("the app") is published by Just Make Apps. This policy explains what data the app handles.

1. Data we collect ourselves
   The app does not collect or store any personal information on our servers. There is no account, no login, and no analytics SDK in the app.

2. Data collected by advertising partners
   The app displays ads through Google AdMob. AdMob may collect:
   - Your device's advertising ID
   - Approximate device information (model, OS version, country)
   - Information about your interactions with ads
   This data is used by Google to serve relevant ads and measure ad performance. You can review and reset your advertising ID, or opt out of personalized ads, in your device's settings (Settings → Google → Ads).
   Google's privacy policy: https://policies.google.com/privacy

3. Children's privacy
   The app is suitable for all ages. We do not knowingly collect personal data from children. Ads served through AdMob are configured to comply with applicable child-directed treatment requirements where relevant.

4. Data deletion
   Because the app does not store personal data on our servers, there is no account data to delete. Game progress is stored locally on your device and is removed when you uninstall the app.

5. Contact
   Questions? Email [YOUR-SUPPORT-EMAIL].
```

---

## 7. Submission workflow — step by step

1. **Play Console → Create app**
   - App name: Gravity Dots
   - Default language: English (US)
   - App or game: **Game**
   - Free or paid: **Free**
   - Accept the declarations.

2. **Set up your app** (left-hand checklist) — work top to bottom:
   - **App access** — All functionality available without special access.
   - **Ads** — **Yes, app contains ads**.
   - **Content rating** — answer the questionnaire (Section 5).
   - **Target audience** — Age 13+ is safest with AdMob; if you target younger, you must comply with Families policy and use Google's child-safe ad SDK.
   - **News app** — No.
   - **COVID-19 contact tracing** — No.
   - **Data safety** — fill from Section 4.
   - **Government apps** — No.
   - **Financial features** — No.
   - **Health** — No.

3. **Store listing**
   - Paste copy from Section 2.
   - Upload icon, feature graphic, screenshots from Section 3.

4. **Production → Create new release**
   - Upload the signed `.aab`.
   - Release name auto-fills (e.g. `1 (1.0)`).
   - Release notes (max 500 chars):
     ```
     First release. 8 levels, calm physics, draw walls to guide dots home.
     ```
   - **Save** → **Review release** → **Start rollout to production**.

5. **Review wait time:** typically 1–7 days for a first submission, faster for subsequent updates.

6. **Common rejection causes (save yourself a week):**
   - Missing privacy policy URL → Section 6.
   - "Contains ads" not declared in console even though ads are visible.
   - Data safety form doesn't list AdMob's data collection.
   - Screenshots include device frames / status bars from other apps / marketing text in a language other than the store-listing language.
   - Test ad units still wired up — Google flags constant unfilled requests.

---

## 8. Post-launch

- **Pre-launch report** (auto-generated by Google on first upload) — check it under **Release → Pre-launch report**. Fix any crash signatures before promoting.
- **Open / closed testing tracks** — useful for next versions; keep production-only for v1 to keep it simple.
- **Reviews & ratings** — reply to early reviews, especially 1–3 star ones. Conversion improves materially after the first 10 reviews.

---

## 9. Quick reference

| Thing | Where |
|---|---|
| Bump version | `app/build.gradle.kts` → `versionCode` / `versionName` |
| Change app name | `app/src/main/res/values/strings.xml` → `app_name` |
| Replace AdMob IDs | `AndroidManifest.xml` + `ads/AdManager.kt` |
| App icon source | `app/src/main/res/mipmap-*` |
| Build release AAB | Android Studio → Build → Generate Signed Bundle |
| Output AAB | `app/release/app-release.aab` |
