package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto;

public enum CryptoConfig {

    KEY_128((byte) 1, 16, 12, 16), // used in Conceal v1
    KEY_256((byte) 2, 32, 12, 16);

    public final byte cipherId;
    public final int keyLength;
    public final int ivLength;
    public final int tagLength;

    CryptoConfig(byte chiperId, int keyLength, int ivLength, int tagLength) {
        this.cipherId = chiperId;
        this.keyLength = keyLength;
        this.ivLength = ivLength;
        this.tagLength = tagLength;
    };
}