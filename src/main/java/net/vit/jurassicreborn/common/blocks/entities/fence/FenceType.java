package net.vit.jurassicreborn.common.blocks.entities.fence;

public enum FenceType
{
        LOW("low_security_fence_base"),
        MED("med_security_fence_base"),
        HIGH("high_security_fence_base");

        private final String path;
        FenceType(String path) { this.path = path; }
        public String getPath() { return path; }
}
