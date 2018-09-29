public class Movie{
	

	public int tid;
	public String movie;
	public int year_of_release;
	public float rating;
	public int no_of_votes;
	public String director;

	public Movie(int tid, String movie, int year_of_release, float rating, int no_of_votes, String director){
		this.tid = tid;
		this.movie = movie;
		this.year_of_release = year_of_release;
		this.rating = rating;
		this.no_of_votes = no_of_votes;
		this.director = director;
	}
}