/**
 Copyright 2015 Olivier Sylvain (aka GotoLink)
 This software is distributed under the terms of the GNU General Public License.
 Please see COPYING for precise license information.

 This file is part of Ancient Warfare.

 Ancient Warfare is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Ancient Warfare is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.shadowmage.ancientwarfare.structure.entity;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.shadowmage.ancientwarfare.core.util.BlockPosition;

public class RotateBoundingBox extends AxisAlignedBB{
    private final static float TO_RAD = (float) Math.PI / 180F;
    private final int facing;
    //From vertical axis
    private float angle;
    public RotateBoundingBox(int face, BlockPosition min, BlockPosition max) {
        this(face, min.x, min.y, min.z, max.x, max.y, max.z);
    }

    private RotateBoundingBox(int face, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.facing = face;
    }

    @Override
    public boolean intersectsWith(AxisAlignedBB mask) {
        if(mask.maxY > minY) {
            double height = MathHelper.cos(angle * TO_RAD) * (maxY - minY);
            if(mask.minY < height + minY) {
                if(facing%2==0){//z
                    if(!(mask.minX < maxX && mask.maxX > minX))
                        return false;
                }else{//x
                    if(!(mask.minZ < maxZ && mask.maxZ > minZ))
                        return false;
                }
                double length = MathHelper.sin(angle * TO_RAD) * (maxY - minY + 1);
                switch (facing) {
                    case 0://z++
                        return mask.minZ < length + minZ && mask.maxZ > minZ;
                    case 1://x--
                        return mask.maxX > maxX - length && mask.minX < maxX;
                    case 2://z--
                        return mask.maxZ > maxZ - length && mask.minZ < maxZ;
                    case 3://x++
                        return mask.minX < length + minX && mask.maxX > minX;
                }
            }
        }
        return false;
    }

    @Override
    public AxisAlignedBB copy() {
        RotateBoundingBox box = new RotateBoundingBox(facing, minX, minY, minZ, maxX, maxY, maxZ);
        box.angle = this.angle;
        return box;
    }

    public void rotate(float increment){
        angle += increment;
    }
}
