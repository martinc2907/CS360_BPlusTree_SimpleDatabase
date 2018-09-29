public class Key{

	// KEY = (Year of Release, No. of Votes)
	// Compare YoR first, then No. of votes
	// Ascending order

	public int release_year;
	public int no_of_votes;

	public Key(int year, int votes){
		release_year = year;
		no_of_votes = votes;
	}

	public boolean smallerThan(Key other){
		if(release_year < other.release_year){
			return true;
		}
		else if(release_year > other.release_year){
			return false;
		}
		else{	//equal
			if(no_of_votes < other.no_of_votes){
				return true;
			}else{
				return false;
			}
		}
	}

	public boolean smallerEqualThan(Key other){
		return (smallerThan(other) || equals(other));
	}

	public boolean largerEqualThan(Key other){
		return !smallerThan(other);
	}

	public boolean equals(Key other){
		if(release_year == other.release_year && no_of_votes == other.no_of_votes){
			return true;
		}else{
			return false;
		}
	}

	public String toString(){
		return "(" + Integer.toString(release_year) + "," + Integer.toString(no_of_votes) + ")";
	}
}