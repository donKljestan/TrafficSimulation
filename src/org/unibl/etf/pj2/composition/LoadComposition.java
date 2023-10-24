package org.unibl.etf.pj2.composition;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.unibl.etf.pj2.Config;

public class LoadComposition extends Thread {

	public LoadComposition() {
		super();
	}
	
	public String getPathDirectory() throws Exception {
		return Config.get(Config.TRAINS);
	}
	
	public void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path watchDir = Paths.get(getPathDirectory());
			watchDir.register(watchService, ENTRY_CREATE);
			
			while(true) {
				WatchKey key = null;
				try {
					key = watchService.take();
				} catch (InterruptedException ex) {
					Logger.getLogger(LoadComposition.class.getName()).log(Level.SEVERE,ex.toString());
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					try (BufferedReader bf = new BufferedReader(new FileReader(new File(
							getPathDirectory() + File.separator + event.context())))) {
						Composition c = new Composition(bf);
						c.start();
						Thread.sleep(100);
					} catch (Exception e) {
						Logger.getLogger(LoadComposition.class.getName()).log(Level.SEVERE,e.toString());
					}
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(LoadComposition.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
}
