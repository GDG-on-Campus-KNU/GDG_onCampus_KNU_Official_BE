package com.gdsc_knu.official_homepage.entity.enumeration;

public enum Track {
    FRONT_END, BACK_END, ANDROID, AI, DESIGNER, UNDEFINED;

    public static Track[] getValidTrack() {
        return new Track[] {
                FRONT_END,
                BACK_END,
                ANDROID,
                AI,
                DESIGNER
        };
    }
}
