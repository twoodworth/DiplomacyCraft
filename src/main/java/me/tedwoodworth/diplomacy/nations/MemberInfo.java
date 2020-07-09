package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;

public class MemberInfo {
    private DiplomacyPlayer member;
    private String memberClassID;

    public MemberInfo(DiplomacyPlayer member, String memberClassID) {
        this.member = member;
        this.memberClassID = memberClassID;
    }

    public String getMemberClassID() {
        return memberClassID;
    }

    public DiplomacyPlayer getMember() {
        return member;
    }

    public void setMemberClassID(String memberClassID) {
        this.memberClassID = memberClassID;
    }
}
