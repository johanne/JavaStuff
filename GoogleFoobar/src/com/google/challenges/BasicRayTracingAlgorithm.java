package com.google.challenges;

public class BasicRayTracingAlgorithm {

	public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
		final double[] endPoint = {badguy_position[0], badguy_position[1]};
		final double[] captainPos = {captain_position[0], captain_position[1]};
		final int bigger = Math.max(dimensions[0], dimensions[1]);
		// studying ray tracing
		// try the basic ray code
		for(int x = -bigger; x <= bigger; x++){
			for (int y = -bigger; y <= bigger; y++){
				double posX = captain_position[0], posY = captain_position[1];  //x and y start position
				double rayDirX = -3.0, rayDirY = -2.0; //initial direction vector
				double planeX = 0.0, planeY = 0.0; //the 2d raycaster version of camera plane
				double dist = 0;
				int w = Math.max(dimensions[0], dimensions[1]);
				
				
				
				while(dist < distance){
			      //calculate ray position and direction
			      //double cameraX = 2*x/(double)(w)-1; //x-coordinate in camera space
			      double rayPosX = posX;
			      double rayPosY = posY;
			      double wallX = 0;
			      //which box of the map we're in
			      int mapX = (int) (rayPosX);
			      int mapY = (int) (rayPosY);
		
			      //length of ray from current position to next x or y-side
			      double sideDistX;
			      double sideDistY;
		
			      //length of ray from one x or y-side to next x or y-side
			      double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
			      double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
		
			      //what direction to step in x or y-direction (either +1 or -1)
			      int stepX;
			      int stepY;
		
			      int hit = 0; //was there a wall hit?
			      int side = 0; //was a NS or a EW wall hit?
		
			      //calculate step and initial sideDist
			      if (rayDirX < 0)
			      {
			        stepX = -1;
			        sideDistX = (rayPosX - mapX) * deltaDistX;
			      }
			      else
			      {
			        stepX = 1;
			        sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
			      }
			      if (rayDirY < 0)
			      {
			        stepY = -1;
			        sideDistY = (rayPosY - mapY) * deltaDistY;
			      }
			      else
			      {
			        stepY = 1;
			        sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
			      }
			      
			    //perform DDA
			      while (hit == 0)
			      {
			        //jump to next map square, OR in x-direction, OR in y-direction
			        if (sideDistX < sideDistY)
			        {
			          sideDistX += deltaDistX;
			          mapX += stepX;
			          side = 0;
			        }
			        else
			        {
			          sideDistY += deltaDistY;
			          mapY += stepY;
			          side = 1;
			        }
			        
			        if(mapX==0)
			        	System.out.println("");
			        //Check if ray has hit a wall
			        if ( (mapX == 0 || mapX == dimensions[0]) && (mapY == 0 || mapY == dimensions[1]))
			        	hit = 1;
			        else if ((mapX == captain_position[0] && mapY == captain_position[1])){
			        	hit = 2; // we hit the captain
			        	dist = distance+1;
			        }
			        else if ((mapX == badguy_position[0] && mapY == badguy_position[1]))
			        	hit = 3; // we hit our badGuy!
			        else if(mapX < 0 || mapY < 0 || mapX > dimensions[0] || mapY > dimensions[1]){
			        	hit = 4;
			        	dist = distance+1;
			        }
			      }
			      
			      if (side == 0) dist += (mapX - rayPosX + (1 - stepX) / 2) / rayDirX;
			      else           dist += (mapY - rayPosY + (1 - stepY) / 2) / rayDirY;
		
			      if (side == 0) wallX = rayPosY + dist * rayDirY;
			      else           wallX = rayPosX + dist * rayDirX;
			      wallX -= Math.floor((wallX));
			      System.out.println(dist);
			}
			}
			}
		return 0;
	}
}
