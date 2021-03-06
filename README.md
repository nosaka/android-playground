# android-playground
android-playground。Android周りで個人的に少し触ってみたものをおいている遊び場です。

## animation

Lollipop以降に登場したいろいろなアニメーションの実装サンプルになります。

<img src="Example/animation.gif" width="180" height="320">

## app_shortcuts

[App Shortcuts](https://developer.android.com/guide/topics/ui/shortcuts.html)のサンプルです。

<img src="Example/app_shortcuts.gif" width="180" height="320">

## barcode

[Barcode API](https://developers.google.com/vision/android/barcodes-overview)のサンプルです。

<img src="Example/barcode.gif" width="180" height="320">

## face

[Face Api](https://developers.google.com/vision/face-detection-concepts)のサンプルです。
顔認識用のアプリです。

<img src="Example/face.gif" width="180" height="320">

## fingerprint

指紋認証APIのサンプルです。

<img src="Example/fingerprint.gif" width="180" height="320">

## nearbymessages

[Nearby Messages API](https://developers.google.com/nearby/messages/overview)のサンプルです。

<img src="Example/nearbymessages.gif" width="180" height="320">

## nfc

NFC APIのサンプルです。

<img src="Example/nfc.gif" width="180" height="320">

## safetynet

[SafetyNet](https://developers.google.com/android/reference/com/google/android/gms/safetynet/SafetyNet)のサンプルです。
アプリケーションをインストールすると、インストールした端末がルート化されているかなどを判断するための情報が表示されます。

<img src="Example/safetynet.gif" width="180" height="320">

## supportsample

あまりメジャーではないサポート系ライブラリ内のAPIのサンプルです。

<img src="Example/supportsample.gif" width="180" height="320">

## textrecognizer
[TextRecognizer](https://developers.google.com/android/reference/com/google/android/gms/vision/text/TextRecognizer)のサンプルです。

<img src="Example/textrecognizer.gif" width="180" height="320">

## lifecycles
[Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle.html)のサンプルです。
使い所としては以下ような使い方で効果を発揮すると思われます。
- FragmentとActivity間のデータ通信(ActivityとActivityは不可)したい場合に簡単に同期できる
- データを監視ができるので、Web APIやデータアクセス時のコールバックを簡易に行える
- 画面オリエンテーションを行う場合のデータ復元などが簡単に行える
- ライフサイクルごとに管理が必要なLocationManagerをActivity毎に一々LocationManager#requestLocationUpdatesしたり、LocationManager#removeUpdatesしていたものを、
ライフサイクルを内包した独自クラスを定義することで一元管理できる

## room
[Room](https://developer.android.com/topic/libraries/architecture/room.html)のサンプルです。
GreenDaoとの登録、取得時の比較ができます。
以下は計測結果の1つです。

|lib|3万件登録|10万件登録(一括)|10万件取得|
|:--:|:--:|:--:|:--:|
|Room|17065ミリ秒|3528ミリ秒|2304ミリ秒|
|GreenDao|228229ミリ秒|4234ミリ秒|3860ミリ秒|





