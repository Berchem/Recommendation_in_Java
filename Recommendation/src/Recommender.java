import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.DoubleStream;



class Recommender {
	String[][] dataset;
	HashMap<Integer, String> unique = new HashMap<Integer, String>();
	public double[][] user_item_matrix;
	public double[][] item_user_matrix;
	public double[][] user_similarity;
	public double[][] item_similarity;

	public Recommender() {
		
	}
	
	public Recommender(String[][] dataset) {
		// overwrite attribute dataset
		this.dataset = dataset;
		
		// overwrite attribute unique
		int ct = 0;
		for (String[] data: dataset)
			for (String ele: data)
				if (!this.unique.containsValue(ele)) {
					this.unique.put(ct, ele);
					ct++;
				}
		
		// overwrite attribute user_item_matrix
		this.user_item_matrix = this._usr_item_map();
		
		int i, j, m = this.user_item_matrix.length, n = this.unique.size();
		
		// overwrite attribute item_user_matrix
		this.item_user_matrix = new double[n][m];
		for (i=0; i<m; i++)
			for (j=0; j<n; j++)
				this.item_user_matrix[j][i] =  this.user_item_matrix[i][j];
		
		// overwrite attribute user_similarity
		this.user_similarity = new double[m][m];
		for (i=0; i<m; i++)
			for (j=0; j<m; j++)
				this.user_similarity[i][j] = this.similarity_calc(this.user_item_matrix[i], this.user_item_matrix[j]);
		
		// overwrite attribute item_similarity
		this.item_similarity = new double[n][n];
		for (i=0; i<n; i++)
			for (j=0; j<n; j++)
				this.item_similarity[i][j] = this.similarity_calc(this.item_user_matrix[i], this.item_user_matrix[j]);
	
	}
	
	static double sum(int[] vector) {
		int result = 0;
		for (int i: vector)
			result += i;
		
		return result;
	
	}
	
	private double dot(double[] p, double[] q) {
		int result = 0;
		for (int i=0; i<p.length; i++)
			result += p[i] * q[i];
		
		return result;
	
	}	
	
	private double similarity_calc(double[] p, double[] q) {
	    return dot(p, q) / Math.sqrt(dot(p, p) * dot(q, q));
	    
	}
	
	private double[] _usr_item_vec(String[] data) {
		HashSet<String> data_hash = new HashSet<String>();
		double[] user_item_vector = new double[this.unique.size()];
		
		for (String ele: data)
			data_hash.add(ele);
		
		for (int i=0; i<this.unique.size(); i++)
			user_item_vector[i] = (data_hash.contains(this.unique.get(i))) ? 1.0 : 0.0;
		
        return user_item_vector;
        
	}

    private double[][] _usr_item_map() {
    	int m = this.dataset.length;
    	int n = this.unique.size();
    	double[][] user_item_matrix = new double[m][n];
    	
    	for (int i=0; i<m; i++)
    		user_item_matrix[i] = _usr_item_vec(this.dataset[i]);
    	
    	return user_item_matrix;
    	
	}
    
    Map<String, Integer> popular() {
		return null;
    	
    }
    
    void most_similar_set_to () {
    	
    }
    
    void user_based() {
    	
    }
	
    void most_similar_item_to() {
    	
    }
    
    void item_based() {
    	
    }
    
}

class MostPopular extends Recommender{
	public MostPopular() {
		
	}
	
	public MostPopular(String[][] dataset) {
		super(dataset);
		
	}
	
	private HashMap<String, Integer> _popular_calc(){
		HashMap<String, Integer> counter = new HashMap<>();
		for (int i=0; i<this.unique.size(); i++)
			counter.put(this.unique.get(i), 0);
		
		for (String[] data: this.dataset)
			for (String ele: data)
				if (counter.containsKey(ele))
					counter.replace(ele, counter.get(ele) + 1);
			
		return counter;
	
	}
	
	Map<String, Integer> popular(){
		Map<String, Integer> suggestions = new LinkedHashMap<>();
		HashMap<String, Integer> counter = this._popular_calc();
		
		counter.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> suggestions.put(x.getKey(), x.getValue()));
		
		return suggestions;
		
	}

}

class UserBased extends Recommender{
	double[][] most_similar_set_to(int usr_id){
		double[][] pairs= new double[10][10];
		return pairs;
	}
	
	double[][] user_based(int usr_id){
		double[][] pairs= new double[10][10];
		return pairs;
	}
}

class ItemBased extends Recommender{
	double[][] most_similar_item_to(int usr_id){
		double[][] pairs= new double[10][10];
		return pairs;
	}
	
	double[][] item_based(int item_id){
		double[][] pairs= new double[10][10];
		return pairs;
	}
}
