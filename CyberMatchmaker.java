public class CyberMatchmaker {

    public static void main(String args[])
    {
        CyberMatchmakerFunctions cmf = new CyberMatchmakerFunctions();
        cmf.gatherMatches();
        cmf.likeMatchTweets();
        cmf.followMatches();
        cmf.mentionMatches();
    }
}
