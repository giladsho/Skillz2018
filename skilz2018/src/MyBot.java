package bots;

import java.util.ArrayList;

import pirates.Asteroid;
import pirates.Capsule;
import pirates.Location;
import pirates.Mothership;
import pirates.Pirate;
import pirates.PirateBot;
import pirates.PirateGame;



abstract class Player{
	public Pirate p;
	protected PirateGame pg;


public Player(Pirate p, PirateGame pgg) {
	this.pg=pgg;
	this.p = p;
}
abstract void play();

// getting location to sail to
public void setSail(Location locSailTo) {
		Location l = p.getLocation().towards(locSailTo, 200);
		p.sail(l);
}

public int isCamperOnMyHome() {
	for(Pirate p: pg.getEnemyLivingPirates()) {
	for(Mothership home : pg.getMyMotherships()) {
		if(p.inRange(home, 500)) {
			return home.id;
		}
	}
	}
	return -1;
}

public boolean isAsteroidInRang(Location target) {
	for(Asteroid a : pg.getLivingAsteroids()) {
		if(this.p.inRange(a, 500) && !this.p.canPush(a)) {
			return false;
		}
		if(this.p.canPush(a)){
		
		if(isCamperOnMyHome()!=-1) {
		
		this.p.push(a, pg.getAllMotherships()[isCamperOnMyHome()]);
		return true;
		}
		else {
			
			if(pg.getEnemyMotherships().length!=0)
		  this.p.push(a, new Location(0,0));
			else
				
				this.p.push(a, target);
	    return true;
		}
		}
				
		
	}
	return false;
}

public Pirate PirateSpwan() {
	int min = Integer.MAX_VALUE;
	Pirate toreturn = null;
	
	for(Pirate p : pg.getAllEnemyPirates()) {
		if(p.turnsToRevive<min && p.turnsToRevive!=0) {
			min = p.spawnTurns;
			toreturn=p;
		}
	}
	return toreturn;
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

public Pirate ClosestPirate() {
	int min = Integer.MAX_VALUE;
	Pirate toreturn =null;
	for(Pirate p : pg.getEnemyLivingPirates()) {
		if(p.distance(this.p)<min) {
			toreturn=p;
			min = p.distance(this.p);
		}
	}
	return toreturn;
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
class CamperKiller extends Player{
   
	public CamperKiller(Pirate p, PirateGame pgg) {
		super(p, pgg);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	void play() {
		// TODO Auto-generated method stub
		 System.out.println("CamperKiller id: " +p.id);
		if(pg.getAllAsteroids().length==0) {
			new Cancer(p, pg);
			return;
		}
		if(this.p.canPush(pg.getAllAsteroids()[0])) {
			
			System.out.println("PUSH ASTROID");
			p.push(pg.getAllAsteroids()[0], new Location(1800,6400));
		}
		else {
			 Location l = p.getLocation().towards(pg.getAllAsteroids()[0], 200);
			 p.sail(l);
		}
	}
	
}
class Attaker extends Player{

	public Attaker(Pirate p, PirateGame pgg) {
		super(p, pgg);
		// TODO Auto-generated constructor stub
	}

	@Override
	void play() {
		// TODO Auto-generated method stub
		
	}
	
}
class Climb extends Player{
	int CapsuleId;
	public Climb(Pirate p, PirateGame pgg,int id) {
		super(p, pgg);
		CapsuleId = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	void play() {
		// TODO Auto-generated method stub
		
	 System.out.println("Climber ("+CapsuleId+") id: " +p.id);
	 Location target = pg.getMyCapsules()[CapsuleId].location;
	 if(p.hasCapsule()) {
		 target = ClosestMother();
	 }
	 if(!isAsteroidInRang(target)) {
	 Location l = p.getLocation().towards(target, 200);
	 p.sail(l);
	 
	 }
	 
	 
	}
	
	public Location ClosestMother() {
		
		int min = Integer.MAX_VALUE;
		Mothership m = null;
		
		for(Mothership mom : pg.getMyMotherships()) {
			if(mom.distance(this.p)<min) {
				min = mom.distance(this.p);
				m=mom;
			}
		}
		return m.location;
	}
	 
	 
 }

 class Cancer extends Player {
	 
 	public Cancer(Pirate p1, PirateGame pg) {
 		super(p1, pg);
 	}
 	@Override
 	public void play() {
 		// TODO Auto-generated method stub
 		
 		 System.out.println("Cancer id: " +p.id);
 		Pirate target = ClosestHolder();
 		if(target==null) {
 			if(PirateSpwan()!=null) {
 			Location l=p.getLocation().towards(PirateSpwan().initialLocation, 200);
			 p.sail(l);
 			}
 			return; 
 		}
 		if(isAsteroidInRang(target.location)) {
 			System.out.println("shot astroidQ!!");
 			return;
 		}
 		if(p.canPush(target)) {
 			p.push(target, WhereToPush(pg, target));
 		}
 		else {
 			Location l=p.getLocation().towards(target, 200);
			 p.sail(l);
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
 	
 	public Pirate ClosestHolder() {
 		int min = Integer.MAX_VALUE;
 		Pirate toretrun = null;
 		ArrayList<Pirate> holders = new ArrayList<Pirate>();
 		
 		for(int i =0; i<pg.getEnemyCapsules().length; i++) {
 			holders.add(pg.getEnemyCapsules()[i].holder);
 		}
 		
 		
 		for(Pirate p : holders) {
 			if(p==null)
 				return ClosestPirate();
 			if(p.distance(this.p) <min) {
 				min = p.distance(this.p);
 				toretrun= p;
 			}
 		}
 		
 		return toretrun;
 	
 	}
 	 

 	  
 	private void twinCapsuleHunter(Pirate p, PirateGame game)
	{
	 if(p.inRange(game.getEnemyMotherships()[0],1000))
	 {
		
		
		if(game.getEnemyCapsules()[0].holder!=null)
		{
			if(p.canPush(game.getEnemyCapsules()[0].holder))
			{
				p.push(game.getEnemyCapsules()[0].holder, WhereToPush(game, game.getEnemyCapsules()[0].holder));
				return;
			}
			if(p.inRange(game.getEnemyCapsules()[0].holder, 2000))
			{
				 Location l=p.getLocation().towards(game.getEnemyCapsules()[0].holder, 200);
				 p.sail(l);
				 return;
			}
			
				
		}
	 } 
	 Location l=p.getLocation().towards(game.getEnemyMotherships()[0], 200);
	 p.sail(l);
	}


 		
 }
public class MyBot implements PirateBot {
	ArrayList<Player> players;
	@Override
	public void doTurn(PirateGame game) throws Exception {
		// TODO Auto-generated method stub
		players = new ArrayList<Player>();
		int count = 0;
		Pirate climber=null;
		Pirate camperKiller = null;
		boolean needTwoClimbers=game.getMyLivingPirates().length>2&&game.getMyCapsules().length>=2;
		if(game.getMyMotherships().length==0) {
			
			if(game.getEnemyCapsules()[0].holder!=null) {
		Pirate p = game.getMyLivingPirates()[0];
		
		if(p.canPush(game.getEnemyCapsules()[0].holder)) {
			p.push(game.getEnemyCapsules()[0].holder, closestEdge(game.getEnemyCapsules()[0].holder.location,game));
		}
		}
		}
		
		else {
			
		if(game.getMyLivingPirates().length!=0){
				climber = closestPirateToCapsul(0, game, null);
				players.add(new Climb(climber, game, 0));
				if(needTwoClimbers){
					Pirate climber2 = closestPirateToCapsul(1, game, climber);
					players.add(new Climb(climber2,game, 1));
				}
		}		
				if(isCamperAtCapsule(game)!=-1 && game.getAllAsteroids().length!=0 && game.rows>7000) {
					
					camperKiller = closestToAstroid(game, isCamperAtCapsule(game));
					if(isUsed(game, camperKiller)==false)
					players.add(new CamperKiller(camperKiller, game));
				}
			for(int i =0; i<game.getMyLivingPirates().length; i++) {
				if(isUsed(game, game.getMyLivingPirates()[i])==false) {
					players.add(new Cancer(game.getMyLivingPirates()[i], game));
				}
			}
		
		}
		
		play(game);
	}
	public Pirate closestPirateToCapsul(int num, PirateGame game, Pirate except) {
	int min = Integer.MAX_VALUE;
	if(game.getMyLivingPirates().length==0) {
		return null;
	}
	Pirate toreturn = game.getMyLivingPirates()[0];
	for(Pirate p: game.getMyLivingPirates()) {
		if(except!=null&&p.equals(except)){
			
		}
		else{
		if(p.distance(game.getMyCapsules()[num])<min) {
			min = p.distance(game.getMyCapsules()[num]);
			toreturn=p;
		}
		}
	}
	return toreturn;
	}
	
	public boolean isUsed(PirateGame game, Pirate player) {
		
		for(Player pl : players) {
			if(pl.p.id==player.id) {
				return true;
			}
		}
		return false;
	}
	
	public void play(PirateGame game) {
		
		for(Player p : players) {
			p.play();
		}
	}
	
	public int isCamperAtCapsule(PirateGame game) {
		
		for(Pirate p : game.getEnemyLivingPirates()) {
			for(Capsule cap : game.getEnemyCapsules()) {
				if(p.inRange(cap, 200)) {
					return cap.id;
				}
			}
		}
		return -1;
	}
	public Pirate closestToAstroid(PirateGame game,int i) {
		int min = Integer.MAX_VALUE;
		Pirate toreturn = null;
		for(Pirate p : game.getMyLivingPirates()) {
			if(p.initialLocation.distance(game.getAllAsteroids()[0])<min) {
				min =p.initialLocation.distance((game.getAllAsteroids()[0]));
				toreturn = p;
			}
		}
		return toreturn;
	}
	
	
	public Location closestEdge(Location loc,PirateGame game) {
		 Location Kirot[] = new Location[4];
		  Kirot[0] = new Location(0,loc.col);
		  Kirot[1] = new Location(loc.row,0);
		  Kirot[2]= new Location(game.rows,loc.col);
		  Kirot[3] = new Location(loc.row,game.cols);
		  Location min = Kirot[0];

		  
		  for(Location l : Kirot) {
		   if(loc.distance(min)>loc.distance(l)) {
		    min=l;
		   }
		   
		  }
		  return min;
		}


}

	