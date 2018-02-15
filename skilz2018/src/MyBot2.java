

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import pirates.*;

/**
 * This is an example for a bot.
 */


 abstract class Players {
	protected boolean MyIpush=true;
	protected Pirate p;
	protected PirateGame pg;


	abstract public void Play();

	// getting pirate
	public Players(Pirate p, PirateGame pgg) {
		pg=pgg;
		this.p = p;
	}

	// getting location to sail to
	public void setSail(Location locSailTo) {
			Location l = p.getLocation().towards(locSailTo, 200);
			p.sail(l);
	}
	public boolean push() {
		if (p.hasCapsule())
			return false;
		for (int i = 0; i < pg.getAllEnemyPirates().length; i++) {
			if (p.canPush(pg.getAllEnemyPirates()[i])) {
				p.push(pg.getAllEnemyPirates()[i],
						closestEdge(pg.getAllEnemyPirates()[i].location));
				return true;
			}
		}
		return false;
	}

	public Location closestEdge(Location loc) {
	 Location Kirot[] = new Location[4];
	  Kirot[0] = new Location(0,loc.col);
	  Kirot[1] = new Location(loc.row,0);
	  Kirot[2]= new Location(6400,loc.col);
	  Kirot[3] = new Location(loc.row,6400);
	  Location min = Kirot[0];

	  
	  for(Location l : Kirot) {
	   if(loc.distance(min)>loc.distance(l)) {
	    min=l;
	   }
	   
	  }
	  return min;
	}
	
}
//----------------------------------------------------------





class Attacker extends Players {
	protected Location dis;

	public Attacker(Pirate pGet, PirateGame pg) {
		super(pGet, pg);
		MyIpush=true;

		Pirate temp = pg.getMyCapsule().holder;
		if (temp == null)
			dis = pg.getMyCapsule().location;
		else
			dis = temp.location;

	}

	int DistToClose = 450;

	@Override
	public void Play() {
		if (p.hasCapsule()) {
			dis = pg.getMyMothership().location;
		}
		if(pg.getMyCapsule().holder!=null&&!p.hasCapsule()){
			if(p.inPushRange(pg.getMyCapsule().holder))
				if(pg.getMyCapsule().holder.distance(pg.getMyMothership())<750&&(p.id==0||p.id==1)){
					p.push(pg.getMyCapsule().holder, pg.getMyMothership());
					MyIpush=false;
					return;
				}
					
		}
		//if (p.distance(getClosestEnemy()) < DistToClose && p.hasCapsule()) {
			//if (null != escape(getClosestEnemy())) {
				//pg.debug("escapppppppppiiiiiiiinnnnnngggg");
				//dis = escape(getClosestEnemy());
		//	}
		//}

		if (!push())
			setSail(dis);
	}

	public Location getClosestEnemy() {
		int min = p.distance(pg.getAllEnemyPirates()[0]);
		Location enemy = pg.getAllEnemyPirates()[0].location;
		for (Pirate e : pg.getAllEnemyPirates()) {
			if (p.distance(e) < min) {
				min = p.distance(e);
				enemy = e.location;
			}
		}
		return enemy;

	}

	/*public Location escape(Location enemyLoc) {
		ArrayList<Location> locs = new ArrayList<Location>();
		locs.add(new Location(p.location.row + 200, p.location.col));
		locs.add(new Location(p.location.row, p.location.col + 200));
		locs.add(new Location(p.location.row, p.location.col - 200));
		locs.add(new Location(p.location.row - 200, p.location.col));
		ArrayList<Location> remove = new ArrayList<Location>();
		for (Location loc : locs) {
			if (loc.row > 6400 || loc.row < 0 || loc.col > 6400 || loc.col < 0) {
				remove.add(loc);
			}
		}
		if (locs.size() <= 0) {
			return null;
		}

		for (Location re : remove)
			locs.remove(re);
		remove = new ArrayList<Location>();
		if (locs.size() <= 0) {
			return null;
		}
		if (locs.size() > 0) {
			if (enemyLoc.inRange(locs.get(0), 300))
				remove.add(locs.get(0));
		}
		if (locs.size() > 0) {
			if (enemyLoc.inRange(locs.get(0), 300))
				remove.add(locs.get(0));
		}

		for (Location re : remove)
			locs.remove(re);
		if (locs.size() <= 0) {
			return null;
		}
		int minn = locs.get(0).distance(enemyLoc);

		Location lose = locs.get(0);
		for (Location loc : locs) {
			if (loc.distance(enemyLoc) < minn) {
				minn = loc.distance(pg.getMyMothership());
				lose = loc;
			}
		}
		if (locs.size() <= 0) {
			return null;
		}
		locs.remove(lose);
		if (locs.size() < 0)
			return null;
		int min = locs.get(0).distance(pg.getMyMothership());
		Location win = locs.get(0);
		if (locs.size() <= 0) {
			return null;
		}
		for (Location loc : locs) {
			if (loc.distance(pg.getMyMothership()) < min) {
				min = loc.distance(pg.getMyMothership());
				win = loc;
			}
		}
		return win;

	}

	// @Override
	/*
	 * public boolean push() { if (pg.getMyCapsule().holder != null) { int c =
	 * 0; int r = 0; if (p.hasCapsule()) return false; for (int i = 0; i <
	 * pg.getAllEnemyPirates().length; i++) { if
	 * (p.canPush(pg.getAllEnemyPirates()[i])) { c =
	 * pg.getAllEnemyPirates()[i].location.col +
	 * pg.getMyCapsule().holder.location.col; r =
	 * pg.getAllEnemyPirates()[i].location.row +
	 * pg.getMyCapsule().holder.location.row; p.push(pg.getAllEnemyPirates()[i],
	 * new Location( r + pg.getMyCapsule().holder .getLocation().row, c +
	 * pg.getMyCapsule().holder .getLocation().col)); return true; } } } else
	 * super.push(); return false; }
	 */

}




//--------------------------------------------------------


 class Cancerfb extends Players {
 	
 	private Pirate p2; 	
 	

 	public Cancer(Pirate p1,Pirate p2, PirateGame pg) {
 		super(p1, pg);
 		this.p2=p2;
 	}
 	@Override
 	public void Play() {
 		// TODO Auto-generated method stub
 		if(p!=null){
 			twinCapsuleHunter(p, pg);	
 		}
     if(p2!=null){
	  twinCapsuleHunter(p2, pg);	
 		  }
 		
 	}
 	public Location WhereToPush(PirateGame game, Pirate enemy) {
 	  	  
 	  	  Location Kirot[] = new Location[4];
 	  	 
 	  	  Kirot[0] = new Location(0,enemy.getLocation().col);
 	  	  Kirot[1] = new Location(enemy.getLocation().row,0);
 	  	  Kirot[2]= new Location(6400,enemy.getLocation().col);
 	  	  Kirot[3] = new Location(enemy.getLocation().row,6400);
 	  	  Location min = Kirot[0];
 	  	  
 	  	  for(Location l : Kirot) {
 	  	   if(enemy.distance(min)>enemy.distance(l)) {
 	  	    min=l;
 	  	   }
 	  	   
 	  	  }
 	  	  return min;
 	  	  
 	  	 }
 	 

 	  
 	private void twinCapsuleHunter(Pirate p, PirateGame game)
	{
	 if(p.inRange(game.getEnemyMothership(),1000))
	 {
		
		
		if(game.getEnemyCapsule().holder!=null)
		{
			if(p.canPush(game.getEnemyCapsule().holder))
			{
				p.push(game.getEnemyCapsule().holder, WhereToPush(game, game.getEnemyCapsule().holder));
				return;
			}
			if(p.inRange(game.getEnemyCapsule().holder, 2000))
			{
				 Location l=p.getLocation().towards(game.getEnemyCapsule().holder, 200);
				 p.sail(l);
				 return;
			}
			
				
		}
	 } 
	 Location l=p.getLocation().towards(game.getEnemyMothership(), 200);
	 p.sail(l);
	}


 		
 }


//--------------------------------------------

 class Defender extends Players {
	protected Location dis;

	public Defender(Pirate pGet, PirateGame pg) {
		super(pGet, pg);
		Pirate temp = pg.getEnemyCapsule().holder;
		if (temp == null)
			dis = pg.getEnemyMothership().location;
		else
			dis = temp.location;

	}

	@Override
	public void Play() {
		if (!push()) {
			setSail(dis);
		}
	}

}

class Patrol extends Defender {
	int Range = 350;
	public Patrol(Pirate pGet, PirateGame pg) {
		super(pGet, pg);
		for (Pirate enemy : pg.getAllEnemyPirates()) {
			if (pg.getEnemyMothership().inRange(enemy, Range)){
				super.dis = enemy.location;
				return;
			}
			
			

		}

	}

}


//----------------------------------------




//--------------------------------------------------------

public class MyBot2 implements PirateBot {
	/**
     * Makes the bot run a single turn.
     *
     * @param game - The current game state.
     */
    @Override
    public void doTurn(PirateGame game) {
    	ArrayList<Players> pirates= new ArrayList<Players>();
    
    	ArrayList<Pirate> CanUse = new ArrayList<Pirate>();
    	for(Pirate p: game.getMyLivingPirates()) {
    		CanUse.add(p);
    	}
    	if(game.getMyCapsule().holder!=null)
    	CanUse.remove(game.getMyCapsule().holder);
    	System.out.println("removed holder from CanUse");
		if(CanUse.size()==1){
			System.out.println("one");
			Pirate one=CanUse.get(0);
			CanUse.remove(CanUse.get(0));
			pirates.add(new Cancer(one, null, game));
		}
		else if(CanUse.size()==2){
			
		System.out.println("two");
			Pirate one=CanUse.get(0);
			Pirate two=CanUse.get(1);
			CanUse.remove(CanUse.get(0));
			CanUse.remove(CanUse.get(0));
			pirates.add(new Cancer(one,two, game));
		}
		else if(CanUse.size()>2){
			System.out.println(CanUse.size());
			Pirate min= CanUse.get(0);
			for(Pirate p: CanUse){
				if(min.distance(game.getEnemyMothership())>p.distance(game.getEnemyMothership()))
					min=p;
			}
			Pirate p1=min;
			CanUse.remove(min);
			min= CanUse.get(0);
			for(Pirate p: CanUse){
				if(min.distance(game.getEnemyMothership())>p.distance(game.getEnemyMothership()))
					min=p;
			}
			Pirate p2=min;
			CanUse.remove(min);
			pirates.add(new Cancer(p1, p2, game));
		}
		
		
    	int i=0,j=0;
   // 	twinCapsuleHunter(game.getMyPirateById(0), game.getMyPirateById(1), game);
    	
    	for (Pirate p: CanUse){
    		
    	//	if(p.id!=0&&p.id!=1){
    		i++;
    			pirates.add(new Attacker(p, game));
    	//	}
    	}
    	if(game.getMyCapsule().holder!=null)
    	pirates.add(new Attacker(game.getMyCapsule().holder,game));
    	for (Players g: pirates){
    		
    		g.Play();
    	}
    	
        // Get one of my pirates.
      /*  for(Pirate pirate: game.getMyLivingPirates()){
        // Try to push, if you didn't - take the capsule and go to the mothership.
        if (!tryPush(pirate, game)) {
            // If the pirate doesn't have a capsule, go and get it!
            if (pirate.capsule == null) {
               Capsule capsule = game.getMyCapsule();
                pirate.sail(capsule);
           }
            // Else, go to my mothership.
            else {
                
                // Go towards the mothership.
                if(!IsNotSafe(game,pirate)){
                pirate.sail(new Location(game.rows,pirate.getLocation().col));
                }
                else{
                    pirate.sail(game.getMyMothership());
                }
                

            }
            
        }

        }*/
    }

	/**
	 * Makes the pirate try to push an enemy pirate. Returns True if it did.
	 *
	 * @param pirate
	 *            - The pushing pirate.
	 * @param game
	 *            - The current game state.
	 * @return - true if the pirate pushed.
	 */
	private boolean tryPush(Pirate pirate, PirateGame game) {
		// Go over all enemies.
		for (Pirate enemy : game.getEnemyLivingPirates()) {
			// Check if the pirate can push the enemy.
			if (pirate.canPush(enemy)) {
				// Push enemy!
				pirate.push(enemy, enemy.initialLocation);

				// Print a message.
				System.out.println("pirate " + pirate + " pushes " + enemy + " towards " + enemy.initialLocation);

				// Did push.
				return true;
			}
		}

		// Didn't push.
		return false;
	}

	private boolean IsNotSafe(PirateGame game, Pirate p) {
		if (p.getLocation().row > game.rows - 300) {
			Location l = p.getLocation().towards(new Location(0, p.getLocation().col), 200);
			p.sail(l);
			return true;
		} else if (p.getLocation().row < 100) {
			Location l = p.getLocation().towards(new Location(0, p.getLocation().col), 200);
			p.sail(l);
			return true;
		}
		return false;
	}
	
	private Pirate getEnemyCarrier(PirateGame game)	{
		 for(Pirate enemy : game.getAllEnemyPirates())
		 {
		  if(enemy.hasCapsule())
		   return enemy;
		 }
		 return null;
		}
		
		public Location WhereToPush(PirateGame game, Pirate enemy) {
	  
	  Location Kirot[] = new Location[4];
	 
	  Kirot[0] = new Location(0,enemy.getLocation().col);
	  Kirot[1] = new Location(enemy.getLocation().row,0);
	  Kirot[2]= new Location(6400,enemy.getLocation().col);
	  Kirot[3] = new Location(enemy.getLocation().row,6400);
	  Location min = Kirot[0];
	  
	  for(Location l : Kirot) {
	   if(enemy.distance(min)>enemy.distance(l)) {
	    min=l;
	   }
	   
	  }
	  return min;
	  
	 }
		private void twinCapsuleHunter(Pirate p1,Pirate p2, PirateGame game)
		{
		 //     
		 if(!p1.getLocation().equals(p2.getLocation()))
		 {
		  Location l=p1.getLocation().towards(p2.getLocation(), 200);
		  p1.sail(l);
		  return;
		 }
		 //       
		 
		  if(game.getEnemyCapsule().holder!=null)
		  {
		  if(p1.canPush(game.getEnemyCapsule().holder))
		   {
		   p1.push(game.getEnemyCapsule().holder,WhereToPush(game, game.getEnemyCapsule().holder));
		   p2.push(game.getEnemyCapsule().holder,WhereToPush(game, game.getEnemyCapsule().holder));
		   return;
		   }
		   else {
		    
		     Location l=p1.getLocation().towards(game.getEnemyCapsule().holder.getLocation(), 200);
		  	  p1.sail(l);
		  	  p2.sail(l);
		    return;
	    }
		  }
		  else
		  {
		   Location l=p1.getLocation().towards(game.getEnemyCapsule().getLocation(), 200);
		  p1.sail(l);
		  p2.sail(l); 
		  }
		 
		 
		}


}