import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JankenGame {
    private static final Scanner STDIN = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static final boolean IS_DEBUG_MODE = true;
    private static final int ONE_LINE = 1;

    // じゃんけんの勝負回数設定
    // 1 にした場合は IS_ROUND_CONFIG_1 を true にする
    private static final int MAX_ROUND_CONFIG = 3;
    private static final boolean IS_ROUND_CONFIG_1 = false;

    // じゃんけん結果（お互いの手）配列のインデックス設定
    private static final int COM = 0;
    private static final int USER = 1;

    private static final int IS_USER_WIN_PATTERN = 1;
    private static final int IS_USER_LOSE_PATTERN = 2;

    private static final boolean WIN = true;
    private static final boolean LOSE = false;

    private static final int ROUND_OF_START = 1;

    private static final String[] HANDS = { "グー", "チョキ", "パー", };
    private static final String MESSAGE_FORMAT_FOR_SELECT_MENU_OPTION = "%d: %s %n";

    private static final String MESSAGE_FORMAT_FOR_SHOW_ROUND = "%d 回目: ========== %n";
    private static final String MESSAGE_FOR_GAME_NAME = "じゃんけん勝負";
    private static final String MESSAGE_FOR_GAME_DESCRIPTION = "グーチョキパーを数字で入力してね";
    private static final String MESSAGE_FOR_REQUIRE_USER_HAND = "最初はぐー、じゃんけん： ";
    private static final String MESSAGE_FOR_REQUIRE_INPUT_WITH_NUM = "数字で入力してください";
    private static final String MESSAGE_FORMAT_FOR_REQUIRE_IN_RANGE = "%d ～ %d の範囲の数字で入力してください %n";
    private static final String MESSAGE_FORMAT_FOR_SHOW_EACH_HANDS = "%s(COM)と%s(Player)で… %n";
    private static final String MESSAGE_FORMAT_FOR_DEBUG_COM_HAND = "[DEBUG] COM の手は %s です %n";
    private static final String MESSAGE_FOR_REQUIRE_USER_HAND_AT_AIKO = "あいこで： ";
    private static final String MESSAGE_FOR_RESULT_AIKO = "あいこだよ！";
    private static final String MESSAGE_FOR_USER_WIN = "あなたの勝ち";
    private static final String MESSAGE_FOR_USER_LOSE = "あなたの負け";
    private static final String MESSAGE_FOR_USER_DRAW = "引き分け";
    private static final String MESSAGE_FORMAT_FOR_COUNT_WIN_LOSE = "%d勝 %d負 で";

    public static void main(String[] args) {
        show(MESSAGE_FOR_GAME_NAME);

        int round = ROUND_OF_START;
        List<Boolean> isWinHistory = new ArrayList<>();

        playJanken(round, isWinHistory);
        showFinalResult(isWinHistory);
    }

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

    private static void showFinalResult(List<Boolean> isWinHistory) {
        if (!IS_ROUND_CONFIG_1) {
            String finalResultMessage = getFinalResultMessage(isWinHistory);
            show(finalResultMessage);
        }
    }

    private static String getFinalResultMessage(List<Boolean> isWinHistory) {

        String message = String.format(MESSAGE_FORMAT_FOR_COUNT_WIN_LOSE,
                countUserWinTimes(isWinHistory),
                countUserLoseTimes(isWinHistory));
        if (isUserTotalWin(isWinHistory)) {
            message += MESSAGE_FOR_USER_WIN;
            return message;
        }

        if (isUserTotalLose(isWinHistory)) {
            message += MESSAGE_FOR_USER_LOSE;
            return message;
        }

        message += MESSAGE_FOR_USER_DRAW;
        return message;
    }

    private static boolean isUserTotalWin(List<Boolean> isWinHistory) {
        return countUserWinTimes(isWinHistory) > countUserLoseTimes(
                isWinHistory);
    }

    private static boolean isUserTotalLose(List<Boolean> isWinHistory) {
        return countUserWinTimes(isWinHistory) < countUserLoseTimes(
                isWinHistory);
    }

    private static int countUserWinTimes(List<Boolean> isWinHistory) {
        int userWinTimes = 0;
        for (boolean isUserWin : isWinHistory) {
            if (isUserWin) {
                userWinTimes++;
            }
        }
        return userWinTimes;
    }

    private static int countUserLoseTimes(List<Boolean> isWinHistory) {
        return MAX_ROUND_CONFIG - countUserWinTimes(isWinHistory);
    }

    private static void showRound(int round) {
        if (IS_ROUND_CONFIG_1) {
            return;
        }
        System.out.format(MESSAGE_FORMAT_FOR_SHOW_ROUND, round);
    }

    private static void updateIsWinHistory(List<Boolean> isWinHistory,
            int[] eachHandsAsIndex) {
        if (isWin(eachHandsAsIndex)) {
            isWinHistory.add(WIN);
            return;
        }
        isWinHistory.add(LOSE);
    }

    private static void showJudgeResultExcludingAiko(int[] eachHandsAsIndex) {
        if (isWin(eachHandsAsIndex)) {
            show(MESSAGE_FOR_USER_WIN);
        }

        if (isLose(eachHandsAsIndex)) {
            show(MESSAGE_FOR_USER_LOSE);
        }
        showBlankLine(ONE_LINE);
    }

    private static boolean isWin(int[] eachHandsAsIndex) {
        return calcEachHandsForJudgement(
                eachHandsAsIndex) == IS_USER_WIN_PATTERN;
    }

    private static boolean isLose(int[] eachHandsAsIndex) {
        return calcEachHandsForJudgement(
                eachHandsAsIndex) == IS_USER_LOSE_PATTERN;
    }

    private static int calcEachHandsForJudgement(int[] eachHandsAsIndex) {

        return (eachHandsAsIndex[COM] - eachHandsAsIndex[USER] + HANDS.length)
                % HANDS.length;
    }

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

    private static int[] getEachHands() {
        int comHand = generateComHand();
        showComHand(IS_DEBUG_MODE, comHand);
        int[] eachHandsAsIndex = { comHand, recieveUserHandAsIndex() };
        return eachHandsAsIndex;
    }

    private static boolean isAiko(int[] eachHandsAsIndex) {
        return eachHandsAsIndex[COM] == eachHandsAsIndex[USER];
    }

    private static int generateComHand() {
        return RANDOM.nextInt(HANDS.length);
    }

    private static int recieveUserHandAsIndex() {
        String inputtedNumStr = recieveInputtedNumStr();

        int inputtedNum = validateInputtedUserHand(inputtedNumStr);
        return inputtedNum;
    }

    private static int validateInputtedUserHand(String inputtedNumStr) {
        if (!isNum(inputtedNumStr)) {
            show(MESSAGE_FOR_REQUIRE_INPUT_WITH_NUM);
            return recieveUserHandAsIndex();
        }

        int inputtedNum = parseToInt(inputtedNumStr);

        int min = 0;
        int max = HANDS.length - 1;
        if (!isInRange(inputtedNum, min, max)) {
            showRequireInRange(min, max);
            return recieveUserHandAsIndex();
        }
        return inputtedNum;
    }

    private static boolean isInRange(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private static boolean isNum(String str) {
        try {
            parseToInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static String recieveInputtedNumStr() {
        return STDIN.nextLine();
    }

    private static int parseToInt(String str) {
        return Integer.parseInt(str);
    }

    private static void show(String message) {
        System.out.println(message);
    }

    private static void showWithNoBreak(String message) {
        System.out.print(message);
    }

    private static void showBlankLine(int numOfLine) {
        for (int i = 0; i < numOfLine; i++) {
            System.out.println();
        }
    }

    private static void showSelectHandMenu() {
        int menuId = 0;
        for (String hand : HANDS) {
            System.out.format(MESSAGE_FORMAT_FOR_SELECT_MENU_OPTION, menuId++,
                    hand);
        }
    }

    private static void showRequireInRange(int min, int max) {
        System.out.format(MESSAGE_FORMAT_FOR_REQUIRE_IN_RANGE, min, max);
    }

    private static void showEachHands(int[] eachHandsAsIndex) {
        System.out.format(MESSAGE_FORMAT_FOR_SHOW_EACH_HANDS,
                HANDS[eachHandsAsIndex[COM]], HANDS[eachHandsAsIndex[USER]]);
    }

    private static void showComHand(boolean isDebugMode, int handIndex) {
        if (!isDebugMode) {
            return;
        }
        showBlankLine(ONE_LINE);
        System.out.format(MESSAGE_FORMAT_FOR_DEBUG_COM_HAND, HANDS[handIndex]);
    }
}
