package com.swarmcraft.betterwatercans;

public class Reference {
	
	public static final String MOD_ID = "bwc";
	public static final String NAME = "Better Water Cans Mod";
	public static final String VERSION = "1.0.0";
	
	
	public static final String CLIENT_PROXY_CLASS = "com.swarmcraft.betterwatercans.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.swarmcraft.betterwatercans.proxy.ServerProxy";

	public enum ModItems {
		WateringCan("wateringCan", "ItemWateringCan");
		
		private String unlocalizedName;
		private String registryName;
		
		ModItems(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		
		}
		
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
	}
	
}
