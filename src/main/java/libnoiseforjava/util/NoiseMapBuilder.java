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

package libnoiseforjava.util;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;

public class NoiseMapBuilder
{

   /// Base class for a noise-map builder
   ///
   /// A builder class builds a noise map by filling it with coherent-noise
   /// values generated from the surface of a three-dimensional mathematical
   /// object.  Each builder class defines a specific three-dimensional
   /// surface, such as a cylinder, sphere, or plane.
   ///
   /// A builder class describes these input values using a coordinate system
   /// applicable for the mathematical object (e.g., a latitude/longitude
   /// coordinate system for the spherical noise-map builder.)  It then
   /// "flattens" these coordinates onto a plane so that it can write the
   /// coherent-noise values into a two-dimensional noise map.
   ///
   /// <b>Building the Noise Map</b>
   ///
   /// To build the noise map, perform the following steps:
   /// - Pass the bounding coordinates to the setBounds() method.
   /// - Pass the noise map size, in points, to the setDestSize() method.
   /// - Pass a NoiseMap object to the setDestNoiseMap() method.
   /// - Pass a noise module (derived from ModuleBase) to the
   ///   setSourceModule() method.
   /// - Call the build() method.
   ///
   /// Note that setBounds() is not defined in the base class; it is
   /// only defined in the derived classes.  This is because each model uses
   /// a different coordinate system.


   /// Count of rows completed
   int callback;

   /// Height of the destination noise map, in points.
   int destHeight;

   /// Width of the destination noise map, in points.
   int destWidth;

   /// Destination noise map that will contain the coherent-noise values.
   NoiseMap destNoiseMap;

   /// Source noise module that will generate the coherent-noise values.
   ModuleBase sourceModule;


   public NoiseMapBuilder () throws ExceptionInvalidParam
   {
      callback = 0;
      destHeight = 0;
      destWidth = 0;
      destNoiseMap = new NoiseMap(1,1);
      sourceModule = new ModuleBase(0);
   }

   public NoiseMapBuilder (int height, int width) throws ExceptionInvalidParam
   {
      callback = 0;
      destHeight = 0;
      destWidth = 0;
      destNoiseMap = new NoiseMap(height,width);
      sourceModule = new ModuleBase(0);
   }

   /// Builds the noise map.
   ///
   /// @pre setBounds() was previously called.
   /// @pre setDestNoiseMap() was previously called.
   /// @pre setSourceModule() was previously called.
   /// @pre The width and height values specified by setDestSize() are
   /// positive.
   /// @pre The width and height values specified by setDestSize() do not
   /// exceed the maximum possible width and height for the noise map.
   ///
   /// @post The original contents of the destination noise map is
   /// destroyed.
   ///
   /// @throw ExceptionInvalidParam See the preconditions.
   /// @throw ExceptionOutOfMemory Out of memory.
   ///
   /// If this method is successful, the destination noise map contains
   /// the coherent-noise values from the noise module specified by
   /// setSourceModule().
   public void build () throws ExceptionInvalidParam
   {
      //override in child classes
   }

   /// Returns the height of the destination noise map.
   ///
   /// @returns The height of the destination noise map, in points.
   ///
   /// This object does not change the height in the destination noise
   /// map object until the build() method is called.
   public double getDestHeight ()
   {
      return destHeight;
   }

   /// Returns the width of the destination noise map.
   ///
   /// @returns The width of the destination noise map, in points.
   ///
   /// This object does not change the height in the destination noise
   /// map object until the build() method is called.
   public double getDestWidth ()
   {
      return destWidth;
   }

   /// Sets the destination noise map.
   ///
   /// @param destNoiseMap The destination noise map.
   ///
   /// The destination noise map will contain the coherent-noise values
   /// from this noise map after a successful call to the build() method.
   ///
   /// The destination noise map must exist throughout the lifetime of
   /// this object unless another noise map replaces that noise map.
   public void setDestNoiseMap (NoiseMap destNoiseMap)
   {
      this.destNoiseMap = destNoiseMap;
   }

   /// Sets the source module.
   ///
   /// @param sourceModule The source module.
   ///
   /// This object fills in a noise map with the coherent-noise values
   /// from this source module.
   ///
   /// The source module must exist throughout the lifetime of this
   /// object unless another noise module replaces that noise module.
   public void setSourceModule (ModuleBase sourceModule)
   {
      this.sourceModule = sourceModule;
   }

   /// Sets the size of the destination noise map.
   ///
   /// @param destWidth The width of the destination noise map, in
   /// points.
   /// @param destHeight The height of the destination noise map, in
   /// points.
   ///
   /// This method does not change the size of the destination noise map
   /// until the build() method is called.
   public void setDestSize (int destWidth, int destHeight)
   {
      this.destWidth  = destWidth ;
      this.destHeight = destHeight;
   }

   public NoiseMap getDestNoiseMap()
   {
      return destNoiseMap;
   }

   void setCallback (int callback)
   {
      this.callback = callback;
   }

}