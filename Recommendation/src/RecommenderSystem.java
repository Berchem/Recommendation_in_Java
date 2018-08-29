import java.util.Map;


public class RecommenderSystem {
	
	public static void main(String[] args) {
		// test data
		String[][] users_interests = {
				{"Hadoop", "Big Data", "HBase", "Java", "Spark", "Storm", "Cassandra"},
				{"NoSQL", "MongoDB", "Cassandra", "HBase", "Postgres"},
				{"Python", "scikit-learn", "scipy", "numpy", "statsmodels", "pandas"},
				{"R", "Python", "statistics", "regression", "probability"},
				{"machine learning", "regression", "decision trees", "libsvm"},
				{"Python", "R", "Java", "C++", "Haskell", "programming languages"},
				{"statistics", "probability", "mathematics", "theory"},
				{"machine learning", "scikit-learn", "Mahout", "neural networks"},
				{"neural networks", "deep learning", "Big Data", "artificial intelligence"},
				{"Hadoop", "Java", "MapReduce", "Big Data"},
				{"statistics", "R", "statsmodels"},
				{"C++", "deep learning", "artificial intelligence", "probability"},
				{"pandas", "R", "Python"},
				{"databases", "HBase", "Postgres", "MySQL", "MongoDB"},
				{"libsvm", "regression", "support vector machines"}
		};
		
//		recommendation system object
		Recommender[] rs = new Recommender[3];
		
//		most popular
		rs[0] = new MostPopular(users_interests);
		Map<String, Integer> popular = ((MostPopular) rs[0]).popular(users_interests[0]);
		System.out.println("\nrecommed user_0 some items accordong to the most popular:");
		for (Map.Entry<String, Integer> item: popular.entrySet())
			System.out.printf("%30s: %d\n",  item.getKey(), item.getValue());
		
//		user-based
		rs[1] = new UserBased(users_interests);
		Map<String, Double> userbased = ((UserBased) rs[1]).user_based(0, false);
		System.out.println("\nrecommed user_0 some items accordong to the user-based collaborative filtering:");
		for (Map.Entry<String, Double> item: userbased.entrySet())
			System.out.printf("%30s: %.3f\n",  item.getKey(), item.getValue());
		
//		item-based
		rs[2] = new ItemBased(users_interests);
		Map<String, Double> itembased = ((ItemBased) rs[2]).item_based(0, false);
		System.out.println("\nrecommed user_0 some items accordong to the item-based collaborative filtering:");
		for (Map.Entry<String, Double> item: itembased.entrySet())
			System.out.printf("%30s: %.3f\n",  item.getKey(), item.getValue());
		
	}

}
