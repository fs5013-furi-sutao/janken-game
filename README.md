## じゃんけんゲーム

じゃんけんゲームのサンプルコードです。クラスを使用していません。

## 実装の内容

以下に [JankenGame.java](./src/JankenGame.java) で行ったロジックや実装を説明します

### main メソッド

じゃんけんを実施して、その結果を表示する

``` java
public static void main(String[] args) {
    show(MESSAGE_FOR_GAME_NAME);

    int round = ROUND_OF_START;
    List<Boolean> isWinHistory = new ArrayList<>();

    playJanken(round, isWinHistory);
    showFinalResult(isWinHistory);
}
```

### playJanken メソッド

定数 MAX_ROUND_CONFIG で設定した回数分だけ、じゃんけん勝負を行う（あいこは、勝負回数に含めない）

``` java
// じゃんけんの勝負回数設定
private static final int MAX_ROUND_CONFIG = 3;

private static void playJanken(int round, List<Boolean> isWinHistory) {
    showRound(round);
    show(MESSAGE_FOR_GAME_DESCRIPTION);
    showSelectHandMenu();
    showBlankLine(ONE_LINE);

    showWithNoBreak(MESSAGE_FOR_REQUIRE_USER_HAND);

    int[] eachHandsAsIndex = getEachHandsWithoutAiko();
    showJudgeResultExcludingAiko(eachHandsAsIndex);
    updateIsWinHistory(isWinHistory, eachHandsAsIndex);
    round++;

    if (round <= MAX_ROUND_CONFIG) {
        playJanken(round, isWinHistory);
        return;
    }
}
```

### getEachHandsWithoutAiko メソッド

コンピュータとユーザのじゃんけんの手を取得する（あいこが続く間は再度、手を取得し続ける）

``` java
private static int[] getEachHandsWithoutAiko() {
    int[] eachHandsAsIndex = getEachHands();
    showEachHands(eachHandsAsIndex);

    if (isAiko(eachHandsAsIndex)) {
        show(MESSAGE_FOR_RESULT_AIKO);
        showBlankLine(ONE_LINE);
        showWithNoBreak(MESSAGE_FOR_REQUIRE_USER_HAND_AT_AIKO);
        return getEachHandsWithoutAiko();
    }
    return eachHandsAsIndex;
}
```

### 実行結果例

``` java
private static final boolean IS_DEBUG_MODE = false;

// じゃんけんの勝負回数設定
// 1 にした場合は IS_ROUND_CONFIG_1 を true にする
private static final int MAX_ROUND_CONFIG = 4;
private static final boolean IS_ROUND_CONFIG_1 = false;
```

定数を上記の設定でコードを実行をした場合

``` console
じゃんけん勝負
1 回目: ==========
グーチョキパーを数字で入力してね
0: グー
1: チョキ
2: パー

最初はぐー、じゃんけん： 1
パー(COM)とチョキ(Player)で… 
あなたの勝ち

2 回目: ==========
グーチョキパーを数字で入力してね
0: グー
1: チョキ
2: パー

最初はぐー、じゃんけん： 2
パー(COM)とパー(Player)で… 
あいこだよ！

あいこで： 2
パー(COM)とパー(Player)で… 
あいこだよ！

あいこで： 1
チョキ(COM)とチョキ(Player)で… 
あいこだよ！

あいこで： B
数字で入力してください
0
チョキ(COM)とグー(Player)で… 
あなたの勝ち

3 回目: ==========
グーチョキパーを数字で入力してね
0: グー
1: チョキ
2: パー

最初はぐー、じゃんけん： 0
チョキ(COM)とグー(Player)で… 
あなたの勝ち

4 回目: ==========
グーチョキパーを数字で入力してね
0: グー
1: チョキ
2: パー

最初はぐー、じゃんけん： 2
パー(COM)とパー(Player)で… 
あいこだよ！

あいこで： 3
0 ～ 2 の範囲の数字で入力してください 
0
チョキ(COM)とグー(Player)で… 
あなたの勝ち

4勝 0敗 であなたの勝ち
```
