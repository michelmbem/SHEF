package net.atlanticbb.tantlinger.io;

public interface CopyMonitor {
    void bytesCopied(int numBytes);
    boolean isCopyAborted();
}
