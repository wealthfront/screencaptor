package com.wealthfront.screencaptor

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException

class ScreenshotDirectoryTest {

  @get:Rule
  val temporaryFolder = TemporaryFolder()

  @Test
  fun ensureScreenshotDirectoryExists_createsMissingDirectory() {
    val screenshotDirectory = File(temporaryFolder.root, "screenshots").absolutePath

    ensureScreenshotDirectoryExists(screenshotDirectory)

    assertThat(File(screenshotDirectory).isDirectory).isTrue()
  }

  @Test
  fun ensureScreenshotDirectoryExists_doesNothingWhenDirectoryAlreadyExists() {
    val screenshotDirectory = temporaryFolder.newFolder("screenshots").absolutePath

    ensureScreenshotDirectoryExists(screenshotDirectory)

    assertThat(File(screenshotDirectory).isDirectory).isTrue()
  }

  @Test
  fun ensureScreenshotDirectoryExists_throwsIllegalStateException_whenDirectoryCannotBeCreated() {
    val blockingFile = temporaryFolder.newFile("not-a-directory")
    val screenshotDirectory = File(blockingFile, "screenshots").absolutePath

    val exception = assertThrows(IllegalStateException::class.java) {
      ensureScreenshotDirectoryExists(screenshotDirectory)
    }

    assertThat(exception).hasMessageThat().isEqualTo(
      "Failed to create screenshot directory for path: $screenshotDirectory"
    )
  }

  @Test
  fun ensureScreenshotDirectoryExists_wrapsCause_whenCreationThrows() {
    val screenshotDirectory = File(temporaryFolder.root, "screenshots").absolutePath
    val cause = IOException("disk full")

    val exception = assertThrows(IllegalStateException::class.java) {
      ensureScreenshotDirectoryExists(screenshotDirectory) { throw cause }
    }

    assertThat(exception).hasMessageThat().isEqualTo(
      "Failed to create screenshot directory for path: $screenshotDirectory"
    )
    assertThat(exception.cause).isSameInstanceAs(cause)
  }
}
