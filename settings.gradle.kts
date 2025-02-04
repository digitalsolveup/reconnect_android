pluginManagement {
	repositories {
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
//		jcenter()
		mavenCentral()
		maven {
			url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
		}
		maven {
			url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
		}
		maven {
			url = uri("https://jitpack.io")
		}
	}
}

rootProject.name = "Re-connect"
include(":app")
