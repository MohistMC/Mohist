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

public class Checkerboard extends ModuleBase
{
   /// Noise module that outputs a checkerboard pattern.
   ///
   /// This noise module outputs unit-sized blocks of alternating values.
   /// The values of these blocks alternate between -1.0 and +1.0.
   ///
   /// This noise module is not really useful by itself, but it is often used
   /// for debugging purposes.
   ///
   /// This noise module does not require any source modules.

   public Checkerboard()    
   {
      super(0);
   }

   public double getValue (double x, double y, double z)
   {
      int ix = (int)(Math.floor(NoiseGen.MakeInt32Range (x)));
      int iy = (int)(Math.floor(NoiseGen.MakeInt32Range (y)));
      int iz = (int)(Math.floor(NoiseGen.MakeInt32Range (z)));

      // original was 
      //(ix & 1 ^ iy & 1 ^ iz & 1)
      // not certain if this duplicates it or not
      if ((ix%2 == 1) ^ (iy%2 ==  1) ^ (iz%2 == 1))
         return -1.0;
      else
         return 1.0;
   }
}

