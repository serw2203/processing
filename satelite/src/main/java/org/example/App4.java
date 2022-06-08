package org.example;

public class App4 {
    static int B = 1;
    static int W = 2;
    static int F = 3;

    static int[] POS = new int[]{
        F, F, F, F, F, F, F, F, F, F,
        F, B, B, B, B, 0, 0, 0, 0, F,
        F, B, B, B, B, 0, 0, 0, 0, F,
        F, B, B, B, B, 0, 0, 0, 0, F,
        F, 0, 0, 0, 0, 0, 0, 0, 0, F,
        F, 0, 0, 0, 0, 0, 0, 0, 0, F,
        F, 0, 0, 0, 0, W, W, W, W, F,
        F, 0, 0, 0, 0, W, W, W, W, F,
        F, 0, 0, 0, 0, W, W, W, W, F,
        F, F, F, F, F, F, F, F, F, F
    };

    static int[] ST_TAB = new int[]{
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 2, 3, 4, 5, 6, 7, 8, 0,
        0, 9, 10, 11, 12, 13, 14, 15, 16, 0,
        0, 10, 17, 18, 19, 20, 21, 22, 23, 0,
        0, 11, 18, 24, 25, 26, 27, 28, 29, 0,
        0, 12, 19, 25, 30, 31, 32, 33, 34, 0,
        0, 13, 20, 26, 31, 401, 402, 403, 404, 0,
        0, 14, 21, 27, 32, 402, 405, 406, 407, 0,
        0, 15, 22, 28, 33, 403, 406, 408, 409, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    static int[] PIECE_TAB = new int[]{
        11, 12, 13, 14,
        21, 22, 23, 24,
        31, 32, 33, 34
    };

    static int[] END_TAB = new int[]{
        65, 66, 67, 68,
        75, 76, 77, 78,
        85, 86, 87, 88
    };

    static int[] DEL_TAB = new int[]{
        -1, 1, -10, 10
    };

    static class TMove {
        int source, dest;
    }

    static class TSet {
        byte[] data = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        void clear() {
            data = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        int include(byte n) {
            return data[n >> 3] |= (1 << (n & 7));
        }

        int in(byte n) {
            return data[n >> 3] & (1 << (n & 7));
        }
    }

    static class Targ {
        int score;
        int ply;
        int max;

        TMove move;
        TSet set;
    }

    static int INFINITY = 30000;

    static int MAX_PLY = 3;

    static int checkEnd() {
        for (int i = 0; i < 12; i++) {
            if (POS[END_TAB[i]] != 0 ) return 0;
        }
        return 1;
    }

    public static void main(String[] args) {


    }
}
