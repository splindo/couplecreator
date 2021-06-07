import twitter4j.*;
import java.util.List;

public class CyberMatchmakerFunctions
{
    //declare needed class variables
    private Twitter twitter;

    QueryResult result;

    Status maleTweet;
    Status femaleTweet;

    //constructor
    public CyberMatchmakerFunctions()
    {
        //This method creates a Twitter instance
        twitter = TwitterFactory.getSingleton();
    }

    //make query to locate the needed tweet
    private void finderQuery(String term)
    {
        Query query = new Query(term);
        query.setCount(1);

        try {
            result = twitter.search(query);
        }catch(TwitterException t)
        {
            String message = t.getErrorMessage();
            System.out.print(message);
        }

        if(confirmNewMatch(query))
        {
            retrieveStatus(term);
        }else
        {
            finderQuery(term);
        }
    }

    //method called to gather the matches
    public void gatherMatches()
    {
        String maleTerm = "I need a girlfriend";
        String femaleTerm = "I need a boyfriend";

        finderQuery(maleTerm);
        finderQuery(femaleTerm);
    }

    //method called to like the tweets that caused their selection
    public void likeMatchTweets()
    {
        try {
            twitter.createFavorite(maleTweet.getId());
            twitter.createFavorite(femaleTweet.getId());
        }catch(TwitterException t)
        {
            String message = t.getErrorMessage();
            System.out.print(message);
        }
    }

    //method called to follow the matches
    public void followMatches()
    {
        try {
            twitter.createFriendship(maleTweet.getUser().getId());
            twitter.createFriendship(femaleTweet.getUser().getId());
        }catch(TwitterException t)
        {
            String message = t.getErrorMessage();
            System.out.print(message);
        }
    }

    //method called to mention the matches
    public void mentionMatches()
    {
        String mention = "Congratulations @" + femaleTweet.getUser().getScreenName() +
                " and @" + maleTweet.getUser().getScreenName() + "! Since you both " +
                "recently tweeted about wanting a boyfriend/girlfriend, you have been " +
                "automatically matched up together. Cool!";

                try {
                    //twitter.sendDirectMessage(xxxxxxxxx, mention);
                    twitter.updateStatus(mention);
                }catch(TwitterException t)
                {
                    String message = t.getErrorMessage();
                    System.out.print(message);
                }
    }

    //method called to confirm that the match hasn't been selected before
    private boolean confirmNewMatch(Query query)
    {
        List<Status> tempList;
        tempList = result.getTweets();

        if(tempList.get(0).isFavorited())
        {
            query.setMaxId(tempList.get(0).getId() - 1);
            return false;
        }else
        {
            return true;
        }
    }

    //method called to grab a tweet that was confirmed by the confirmNewMatch method
    private void retrieveStatus(String term)
    {
        List<Status> tempList;
        tempList = result.getTweets();

        if(tempList.get(0).isRetweet())
        {
            tempList.set(0, tempList.get(0).getRetweetedStatus());
        }

        if(term.contains("boy"))
        {
            femaleTweet = tempList.get(0);
        }else
        {
            maleTweet = tempList.get(0);
        }
    }
}