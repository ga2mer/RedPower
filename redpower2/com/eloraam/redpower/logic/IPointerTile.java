package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Quat;

public interface IPointerTile
{
    float getPointerDirection(float var1);

    Quat getOrientationBasis();
}
