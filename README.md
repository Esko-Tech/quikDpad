<div align="center">
<h1>quikDpad</h1>

### SMS and MMS for Android phones with a D-pad and a small screen

</div>

quikDpad is an SMS and MMS app for Android 6.0 and later, built on [QUIK](https://github.com/quik-sms/quik) for flip phones and other phones driven by hardware keys. The inbox, conversations, the drawer and settings work with the D-pad, and screens narrower than 320dp get a scaled layout. It keeps QUIK's features: scheduled and delayed messages, backup and restore, blocking, archiving, pinning, swipe actions, quick reply, voice messages, attachments of any file type and emoji reactions. The app installs as `com.eskotech.quikdpad` and shows as QUIK in the launcher.

## Hardware keys

| Key | What it does |
|---|---|
| D-pad | Moves focus. Down from the toolbar jumps to the first item below it. In a conversation, up and down scroll through a message taller than the screen before moving to the next message. |
| Center or Enter | Clicks the focused item. |
| Long-press center or Enter | Long-clicks the focused item, for example to select conversations or messages. |
| Menu or left soft key | Opens or closes the drawer in the inbox. Opens conversation details in a conversation. |
| Back | Closes the drawer, the attachment menu or a message selection first, then goes back. |

The key handling is mainly in `QkActivity`, `MainActivity`, `ComposeActivity`, `ComposeViewModel`, `MessagesAdapter` and `QkEditText`.

## Small screens

- **Scaled layout.** When the screen's smallest width is under 320dp (a 240x320 screen at 160dpi is 240dp wide), each screen lays out as if it were 320dp wide. Text shrinks half as much as the rest. `SmallScreenScaler` does this; wider screens are left as they are.
- **A switch to turn it off.** **Settings > Appearance > Fit to small screen** turns scaling off. It only shows on screens narrow enough to be scaled.
- **Room for the messages.** The attachment menu scrolls when its six options don't fit. In existing group conversations the "Send as group message" switch is in conversation details, not above the messages.

## Install

Download the APK from [Releases](https://github.com/Esko-Tech/quikDpad/releases), install it and set it as the default SMS app. Releases are signed with this project's own key and use their own application ID, so they install next to other QUIK builds and only update from quikDpad releases.

## Build

Build with JDK 17 and an Android SDK with platform 34; kapt on Kotlin 1.7 fails under JDK 21.

```bash
./gradlew :presentation:assembleDebug   # presentation/build/outputs/apk/debug/QUIK-v<version>-debug.apk
./gradlew lint test                     # lint and unit tests, as run on pull requests into master
```

Debug builds install as `com.eskotech.quikdpad.debug`, next to a release build.

## Releases

- **Branches.** Work lands on `develop`; `master` holds what is released.
- **Releasing.** Merging a pull request from `develop` into `master` runs [`release-from-develop.yml`](.github/workflows/release-from-develop.yml). It builds a signed release APK and publishes a GitHub release tagged `v<versionName>` from `presentation/build.gradle`, with the APK, its SHA-256 checksum and the merged commits. If that tag exists, it first bumps the patch number and `versionCode` and commits the bump to `master`. Afterwards it fast-forwards `develop` to `master`.
- **Signing.** The release APK is signed with the `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS` and `ANDROID_KEY_PASSWORD` repository secrets.
- **Checks.** Pull requests into `master` are built, linted and tested by [`build-on-pull.yml`](.github/workflows/build-on-pull.yml).

## Thank you

A special thank you to Jake ([@klinker41](https://github.com/klinker41)) and Luke Klinker ([@klinker24](https://github.com/klinker24)) for their work on [android-smsmms](https://github.com/klinker41/android-smsmms), which has been an unspeakably large help in implementing MMS into QUIK.

A giant thank you to Moez [moezbhatti](https://github.com/moezbhatti) for creating and maintaining QKSMS, of which QUIK would not exist without.

A big thank you to jmtditech ([@JMTDI](https://github.com/JMTDI)) for building most of the D-pad navigation in [quik-dpad](https://github.com/JMTDI/quik-dpad), which makes this app usable on phones with hardware keys.

## License

Licensed under the GNU General Public License v3.0 ([`LICENSE`](LICENSE)).
