import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.DoubleStream;

public class RecommenderSystem {
	static void show2darray(String[][] Array2D) {
		for (String[] row: Array2D)
			for (int i=0; i<row.length; i++)
				System.out.printf("%s%s", row[i], (i==row.length-1) ? '\n' : ", ");
	}
	
	static void show2darray(double[][] Array2D) {
		for (double[] row: Array2D)
			for (int i=0; i<row.length; i++)
				System.out.printf("%.3f%s", row[i], (i==row.length-1) ? '\n' : ", ");
	}

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
		
//		show2darray(users_interests);
		double[] d = {1.1, 2.2, 3.3};
		System.out.println(DoubleStream.of(d).sum());
		
		HashMap<Integer, String> set = new HashMap<Integer, String>();
		int ct = 0;
		for (String[] user_interests: users_interests)
			for (String user_interest: user_interests)
				if (!set.containsValue(user_interest)) {
					set.put(ct, user_interest);
					ct++;
				}
                    
		MostPopular popular = new MostPopular(users_interests);
		Map<String, Integer> counter = popular.popular();
		System.out.println(counter);
		
		Recommender rs = new Recommender(users_interests);
		
		System.out.println(rs.unique.containsKey(1));
		show2darray(rs.user_similarity);
	}

}
