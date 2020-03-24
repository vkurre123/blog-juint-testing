package com.mydomain;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RepeatedMocker {
    @Test
    public void getsRightVersionWithNoRepeat() throws Exception {
        VersionManager versionManager = Mockito.mock(VersionManager.class);
        doReturn(1).when(versionManager).getVersion();
        SystemUnderTest systemUnderTest = new SystemUnderTest(versionManager);
        Optional<Integer> version = systemUnderTest.getVersion();
        assertThat(version.get()).isEqualTo(1);
        verify(versionManager).getVersion();
    }

    @Test
    public void getsRightVersionWithRepeat() throws Exception {
        VersionManager versionManager = Mockito.mock(VersionManager.class);
        doReturn(-1).doReturn(1).when(versionManager).getVersion();
        SystemUnderTest systemUnderTest = new SystemUnderTest(versionManager);
        Optional<Integer> version = systemUnderTest.getVersion();
        assertThat(version.get()).isEqualTo(1);
        verify(versionManager, times(2)).getVersion();
    }

    @Test
    public void failsGettingRightVersion() throws Exception {
        VersionManager versionManager = Mockito.mock(VersionManager.class);
        doReturn(-1).doReturn(-1).when(versionManager).getVersion();
        SystemUnderTest systemUnderTest = new SystemUnderTest(versionManager);
        Optional<Integer> version = systemUnderTest.getVersion();
        assertThat(version.isPresent()).isFalse();
        verify(versionManager, times(2)).getVersion();
    }

    private static class SystemUnderTest {
        private VersionManager versionManager;

        SystemUnderTest(VersionManager versionManager) {
            this.versionManager = versionManager;
        }

        Optional<Integer> getVersion() {
            int version = versionManager.getVersion();
            if (version == -1) {
                System.out.println("Trying once more, version can't be 1 ....");
                version = versionManager.getVersion();
                if (version == -1) {
                    System.out.println("Error: version can't be 1!");
                    return Optional.empty();
                }
            }
            return Optional.of(version);
        }
    }

    private static class VersionManager {
        int getVersion() {
            return Math.random() < 0.5 ? 1 : -1;
        }
    }
}
