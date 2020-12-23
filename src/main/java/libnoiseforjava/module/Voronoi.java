/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright © 2010 Thomas J. Hodge (java port of libnoise)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at 
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be 
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package libnoiseforjava.module;

import libnoiseforjava.NoiseGen;

public class Voronoi extends ModuleBase
{

   /// Noise module that outputs Voronoi cells.
   ///
   /// In mathematics, a <i>Voronoi cell</i> is a region containing all the
   /// points that are closer to a specific <i>seed point</i> than to any
   /// other seed point.  These cells mesh with one another, producing
   /// polygon-like formations.
   ///
   /// By default, this noise module randomly places a seed point within
   /// each unit cube.  By modifying the <i>frequency</i> of the seed points,
   /// an application can change the distance between seed points.  The
   /// higher the frequency, the closer together this noise module places
   /// the seed points, which reduces the size of the cells.  To specify the
   /// frequency of the cells, call the setFrequency() method.
   ///
   /// This noise module assigns each Voronoi cell with a random constant
   /// value from a coherent-noise function.  The <i>displacement value</i>
   /// controls the range of random values to assign to each cell.  The
   /// range of random values is +/- the displacement value.  Call the
   /// setDisplacement() method to specify the displacement value.
   ///
   /// To modify the random positions of the seed points, call the SetSeed()
   /// method.
   ///
   /// This noise module can optionally add the distance from the nearest
   /// seed to the output value.  To enable this feature, call the
   /// enableDistance() method.  This causes the points in the Voronoi cells
   /// to increase in value the further away that point is from the nearest
   /// seed point.
   ///
   /// Voronoi cells are often used to generate cracked-mud terrain
   /// formations or crystal-like textures
   ///
   /// This noise module requires no source modules.


   /// Default displacement to apply to each cell for the
   /// Voronoi noise module.
   final static double DEFAULT_VORONOI_DISPLACEMENT = 1.0;

   /// Default frequency of the seed points for the Voronoi
   /// noise module.
   final static double DEFAULT_VORONOI_FREQUENCY = 1.0;

   /// Default seed of the noise function for the Voronoi
   /// noise module.
   final static int DEFAULT_VORONOI_SEED = 0;

   private static final double SQRT_3 = 1.7320508075688772935;


   /// Scale of the random displacement to apply to each Voronoi cell.
   double displacement;

   /// Determines if the distance from the nearest seed point is applied to
   /// the output value.
   boolean enableDistance;

   /// Frequency of the seed points.
   double frequency;

   /// Seed value used by the coherent-noise function to determine the
   /// positions of the seed points.
   int seed;


   public Voronoi ()
   {
      super(0);
      displacement = DEFAULT_VORONOI_DISPLACEMENT;
      enableDistance = false;
      frequency = DEFAULT_VORONOI_FREQUENCY;
      seed = DEFAULT_VORONOI_SEED;
   }

   public double getValue (double x, double y, double z)
   {
      // This method could be more efficient by caching the seed values.  Fix
      // later.

      x *= frequency;
      y *= frequency;
      z *= frequency;

      int xInt = (x > 0.0? (int)x: (int)x - 1);
      int yInt = (y > 0.0? (int)y: (int)y - 1);
      int zInt = (z > 0.0? (int)z: (int)z - 1);

      double minDist = 2147483647.0;
      double xCandidate = 0;
      double yCandidate = 0;
      double zCandidate = 0;

      // Inside each unit cube, there is a seed point at a random position.  Go
      // through each of the nearby cubes until we find a cube with a seed point
      // that is closest to the specified position.
      for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++)
      {
         for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++)
         {
            for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++)
            {
               // Calculate the position and distance to the seed point inside of
               // this unit cube.
               double xPos = xCur + NoiseGen.ValueNoise3D (xCur, yCur, zCur, seed);
               double yPos = yCur + NoiseGen.ValueNoise3D (xCur, yCur, zCur, seed + 1);
               double zPos = zCur + NoiseGen.ValueNoise3D (xCur, yCur, zCur, seed + 2);
               double xDist = xPos - x;
               double yDist = yPos - y;
               double zDist = zPos - z;
               double dist = xDist * xDist + yDist * yDist + zDist * zDist;

               if (dist < minDist)
               {
                  // This seed point is closer to any others found so far, so record
                  // this seed point.
                  minDist = dist;
                  xCandidate = xPos;
                  yCandidate = yPos;
                  zCandidate = zPos;
               }
            }
         }
      }

      double value;
      if (enableDistance)
      {
         // Determine the distance to the nearest seed point.
         double xDist = xCandidate - x;
         double yDist = yCandidate - y;
         double zDist = zCandidate - z;
         value = (Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist)
         ) * SQRT_3 - 1.0;
      } else {
         value = 0.0;
      }

      // Return the calculated distance with the displacement value applied.
      return value + (displacement * (double)NoiseGen.ValueNoise3D (
            (int)(Math.floor (xCandidate)),
            (int)(Math.floor (yCandidate)),
            (int)(Math.floor (zCandidate)), seed));// added seed here, not in original
                                                   // but there isn't a working method
                                                   // without seed
   }

   /// Enables or disables applying the distance from the nearest seed
   /// point to the output value.
   ///
   /// @param enable Specifies whether to apply the distance to the
   /// output value or not.
   ///
   /// Applying the distance from the nearest seed point to the output
   /// value causes the points in the Voronoi cells to increase in value
   /// the further away that point is from the nearest seed point.
   /// Setting this value to @a true (and setting the displacement to a
   /// near-zero value) causes this noise module to generate cracked mud
   /// formations.
   public void enableDistance (boolean enable)
   {
      enableDistance = enable;
   }

   /// Returns the displacement value of the Voronoi cells.
   ///
   /// @returns The displacement value of the Voronoi cells.
   ///
   /// This noise module assigns each Voronoi cell with a random constant
   /// value from a coherent-noise function.  The <i>displacement
   /// value</i> controls the range of random values to assign to each
   /// cell.  The range of random values is +/- the displacement value.
   public double getDisplacement ()
   {
      return displacement;
   }

   /// Returns the frequency of the seed points.
   ///
   /// @returns The frequency of the seed points.
   ///
   /// The frequency determines the size of the Voronoi cells and the
   /// distance between these cells.
   public double GetFrequency ()
   {
      return frequency;
   }

   /// Returns the seed value used by the Voronoi cells
   ///
   /// @returns The seed value.
   ///
   /// The positions of the seed values are calculated by a
   /// coherent-noise function.  By modifying the seed value, the output
   /// of that function changes.
   public int getSeed ()
   {
      return seed;
   }

   /// Determines if the distance from the nearest seed point is applied
   /// to the output value.
   ///
   /// @returns
   /// - @a true if the distance is applied to the output value.
   /// - @a false if not.
   ///
   /// Applying the distance from the nearest seed point to the output
   /// value causes the points in the Voronoi cells to increase in value
   /// the further away that point is from the nearest seed point.
   public boolean IsDistanceEnabled ()
   {
      return enableDistance;
   }

   /// Sets the displacement value of the Voronoi cells.
   ///
   /// @param displacement The displacement value of the Voronoi cells.
   ///
   /// This noise module assigns each Voronoi cell with a random constant
   /// value from a coherent-noise function.  The <i>displacement
   /// value</i> controls the range of random values to assign to each
   /// cell.  The range of random values is +/- the displacement value.
   public void setDisplacement (double displacement)
   {
      this.displacement = displacement;
   }

   /// Sets the frequency of the seed points.
   ///
   /// @param frequency The frequency of the seed points.
   ///
   /// The frequency determines the size of the Voronoi cells and the
   /// distance between these cells.
   public void setFrequency (double frequency)
   {
      this.frequency = frequency;
   }

   /// Sets the seed value used by the Voronoi cells
   ///
   /// @param seed The seed value.
   ///
   /// The positions of the seed values are calculated by a
   /// coherent-noise function.  By modifying the seed value, the output
   /// of that function changes.
   public void setSeed (int seed)
   {
      this.seed = seed;
   }

}
