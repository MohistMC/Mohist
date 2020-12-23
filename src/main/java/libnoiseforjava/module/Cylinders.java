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

public class Cylinders extends ModuleBase
{
   /// Noise module that outputs concentric cylinders.
   ///
   /// This noise module outputs concentric cylinders centered on the origin.
   /// These cylinders are oriented along the @a y axis similar to the
   /// concentric rings of a tree.  Each cylinder extends infinitely along
   /// the @a y axis.
   ///
   /// The first cylinder has a radius of 1.0.  Each subsequent cylinder has
   /// a radius that is 1.0 unit larger than the previous cylinder.
   ///
   /// The output value from this noise module is determined by the distance
   /// between the input value and the the nearest cylinder surface.  The
   /// input values that are located on a cylinder surface are given the
   /// output value 1.0 and the input values that are equidistant from two
   /// cylinder surfaces are given the output value -1.0.
   ///
   /// An application can change the frequency of the concentric cylinders.
   /// Increasing the frequency reduces the distances between cylinders.  To
   /// specify the frequency, call the setFrequency() method.
   ///
   /// This noise module, modified with some low-frequency, low-power
   /// turbulence, is useful for generating wood-like textures.
   ///
   /// This noise module does not require any source modules.

   /// Default frequency value for the Cylinders noise module.
   static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;

   /// Frequency of the concentric cylinders.
   double frequency;

   public Cylinders ()
   {
      super(0);
      frequency = DEFAULT_CYLINDERS_FREQUENCY;
   }

   public double getValue (double x, double y, double z)
   {
      x *= frequency;
      z *= frequency;

      double distFromCenter = Math.sqrt(x * x + z * z);
      double distFromSmallerSphere = distFromCenter - Math.floor(distFromCenter);
      double distFromLargerSphere = 1.0 - distFromSmallerSphere;
      double nearestDist = Math.min(distFromSmallerSphere, distFromLargerSphere);

      // Puts it in the -1.0 to +1.0 range.
      return 1.0 - (nearestDist * 4.0);
   }

   /// Returns the frequency of the concentric cylinders.
   ///
   /// @returns The frequency of the concentric cylinders.
   ///
   /// Increasing the frequency increases the density of the concentric
   /// cylinders, reducing the distances between them.
   public double getFrequency() 
   {
      return frequency;
   }

   /// Sets the frequency of the concentric cylinders.
   ///
   /// @param frequency The frequency of the concentric cylinders.
   ///
   /// Increasing the frequency increases the density of the concentric
   /// cylinders, reducing the distances between them.
   public void setFrequency (double frequency)
   {
      this.frequency = frequency;
   }

}
