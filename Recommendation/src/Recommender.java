import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.DoubleStream;



class Recommender {
	String[][] dataset;
	HashMap<Integer, String> unique = new HashMap<Integer, String>();
	protected double[][] user_item_matrix;
	protected double[][] item_user_matrix;
	protected double[][] user_similarity;
	protected double[][] item_similarity;
	private Map<String, Double> suggestions;

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
    
//    void popular() {
//    	
//    }
//    
//    void most_similar_set_to () {
//    	
//    }
//    
//    void user_based() {
//    	
//    }
//	
//    void most_similar_item_to() {
//    	
//    }
//    
//    void item_based() {
//    	
//    }
    
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
	
	Map<String, Integer> popular(String[] data){
		Map<String, Integer> suggestions = new LinkedHashMap<>();
		HashMap<String, Integer> counter = _popular_calc();
		
		counter.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> suggestions.put(x.getKey(), x.getValue()));
		
		for (String item: data)
			suggestions.remove(item);
		
		return suggestions;
		
	}

}

class UserBased extends Recommender{
	UserBased() {
		
	}
	
	UserBased(String[][] dataset) {
		super(dataset);
	}
	
	HashMap<Integer, Double> similar_set_to(int usr_id){
		HashMap<Integer, Double> pairs = new HashMap<Integer, Double>();
		double[] user_similarity = this.user_similarity[usr_id];
		for (int i=0; i<user_similarity.length; i++) 
			if (usr_id != i & user_similarity[i] > 0) 
				pairs.put(i, user_similarity[i]);
		
		return pairs;
		
	}
	
	Map<Integer, Double> most_similar_set_to(int usr_id){
		Map<Integer, Double> pairs_sorted = new LinkedHashMap<>();
		HashMap<Integer, Double> pairs = similar_set_to(usr_id);

		pairs.entrySet().stream()
        .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
        .forEachOrdered(x -> pairs_sorted.put(x.getKey(), x.getValue()));
		
		return pairs_sorted;
		
	}
	
	Map<Integer, Double> most_similar_set_to(int usr_id, int n){
		Map<Integer, Double> pairs_sorted_n = new LinkedHashMap<>();
		Map<Integer, Double> pairs_sorted = most_similar_set_to(usr_id);
		int lim = 0;
		for (Map.Entry<Integer, Double> usr_simi: pairs_sorted.entrySet()) {
			pairs_sorted_n.put(usr_simi.getKey(), usr_simi.getValue());
			lim++;
			if (lim == n) {break;}
		}
		
		return pairs_sorted_n;
		
	}
	
	Map<String, Double> user_based(int usr_id){
		Map<String, Double> suggestions_sorted= new LinkedHashMap<>();
		HashMap<String, Double> suggestions = new HashMap<String, Double>();
		Map<Integer, Double> pairs_sorted = most_similar_set_to(usr_id);
		
		for (Map.Entry<Integer, Double> usr_simi: pairs_sorted.entrySet()) 
			for (String item: this.dataset[usr_simi.getKey()]) 
				if (suggestions.containsKey(item))
					suggestions.replace(item, suggestions.get(item) + usr_simi.getValue());
				else
					suggestions.put(item, usr_simi.getValue());
			
		suggestions.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .forEachOrdered(x -> suggestions_sorted.put(x.getKey(), x.getValue()));
		
		return suggestions_sorted;
		
	}
	
	Map<String, Double> user_based(int usr_id, boolean include_current_items){
		Map<String, Double> suggestions_sorted_include = user_based(usr_id);
		
		if (!include_current_items)		
			for (String item: this.dataset[usr_id]) 
				if (suggestions_sorted_include.containsKey(item))
					suggestions_sorted_include.remove(item);
				
		return suggestions_sorted_include;
		
	}
	
}

class ItemBased extends Recommender{
	ItemBased() {
		
	}
	
	ItemBased(String[][] dataset) {
		super(dataset);
		
	}
	
	HashMap<String, Double> similar_item_to(int item_id){
		HashMap<String, Double> pairs= new HashMap<String, Double>();
		double[] item_similarity = this.item_similarity[item_id];
		for (int i=0; i<item_similarity.length; i++) 
			if (item_id != i & item_similarity[i] > 0) 
				pairs.put(this.unique.get(i), item_similarity[i]);
		
		return pairs;
	}
	
	Map<String, Double> most_similar_item_to(int item_id){
		Map<String, Double> pairs_sorted = new LinkedHashMap<>();
		HashMap<String, Double> pairs = similar_item_to(item_id);

		pairs.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .forEachOrdered(x -> pairs_sorted.put(x.getKey(), x.getValue()));
		
		return pairs_sorted;
		
	}
	
	Map<String, Double> most_similar_item_to(int item_id, int n){
		Map<String, Double> pairs_sorted_n = new LinkedHashMap<>();
		Map<String, Double> pairs_sorted = most_similar_item_to(item_id);
		int lim = 0;
		for (Map.Entry<String, Double> item: pairs_sorted.entrySet()) {
			pairs_sorted_n.put(item.getKey(), item.getValue());
			lim += 1;
			if (lim == n) {break;}
		}

		return pairs_sorted_n;
		
	}
	
	Map<String, Double> item_based(int usr_id){
		Map<String, Double> suggestions_sorted = new LinkedHashMap<>();
		HashMap<String, Double> suggestions = new HashMap<String, Double>();
		
		for (int i=0; i<this.user_item_matrix[usr_id].length; i++) 
			if (this.user_item_matrix[usr_id][i] > 0) {
				Map<String, Double> similar_items = most_similar_item_to(i);
				for (Map.Entry<String, Double> item: similar_items.entrySet())
					if (suggestions.containsKey(item.getKey()))
						suggestions.replace(item.getKey(), suggestions.get(item.getKey()) + item.getValue());
					else
						suggestions.put(item.getKey(), item.getValue());
			}
		
		suggestions.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .forEachOrdered(x -> suggestions_sorted.put(x.getKey(), x.getValue()));
		
		return suggestions_sorted;
		
	}
	
	Map<String, Double> item_based(int usr_id, boolean include_current_items){
		Map<String, Double> suggestions_sorted_include = item_based(usr_id);
		
		if (!include_current_items)
			for (String item: this.dataset[usr_id])
				if (suggestions_sorted_include.containsKey(item))
					suggestions_sorted_include.remove(item);
		
		return suggestions_sorted_include;
		
	}
}
