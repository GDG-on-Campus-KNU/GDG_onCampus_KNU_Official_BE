package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.service.discord.DiscordClient;
import com.gdsc_knu.official_homepage.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberCheckController {
    private final AdminMemberService adminMemberService;
    private final DiscordClient discordClient;

    @GetMapping("members")
    public String getHtml(Model model) {
        model.addAttribute("tracks", Track.values());
        return "members";
    }

    @GetMapping("api/member/check")
    public String getMemberByTrack(@RequestParam Track track, Model model) {
        List<MemberResponse.WithTrack> response = adminMemberService.getMembersByTrack(track);
        model.addAttribute("tracks", Track.values());
        model.addAttribute("defaultTrack", track);
        model.addAttribute("members", response);
        return "members";
    }

    @GetMapping("api/member/check/attendance")
    public ResponseEntity<String> check(@RequestParam List<String> memberNames,
                                        @RequestParam Track track) {
        discordClient.sendAttendance(memberNames, track.name());
        return ResponseEntity.ok().body("출첵 완료!");
    }
}
