package com.swarmcraft.betterwatercans.proxy;

import com.swarmcraft.betterwatercans.init.ModItems;

public class ClientProxy implements CommonProxy {

	public void init() {
		ModItems.registerRenders();
	}
	
}
