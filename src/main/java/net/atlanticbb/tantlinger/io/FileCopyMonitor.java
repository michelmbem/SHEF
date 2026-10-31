package net.atlanticbb.tantlinger.io;

import java.io.File;

public interface FileCopyMonitor extends CopyMonitor {
    void copyingFile(File f);
}
