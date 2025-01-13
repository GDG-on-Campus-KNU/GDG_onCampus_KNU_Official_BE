package com.gdsc_knu.official_homepage.dto.admin.application;

import com.gdsc_knu.official_homepage.entity.enumeration.Track;

public class ApplicationTrackType {
    Track track;
    Long count;

    public String getTrack() {
        return track.name();
    }
    public Integer getCount() {
        return count.intValue();
    }
}
