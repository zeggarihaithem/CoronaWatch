package solutus.coronawatch

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import solutus.coronawatch.data.network.NetworkConnexion
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.network.implementation.ReportApi
import solutus.coronawatch.data.network.implementation.RobotApi
import solutus.coronawatch.data.network.implementation.UserApi
import solutus.coronawatch.data.reposetory.implementation.*
import solutus.coronawatch.ui.mainActivity.home.news.listVideos.ListVideosYoutubeViewModel
import solutus.coronawatch.ui.mainActivity.home.novelties.listArticles.NoveltiesViewModel
import solutus.coronawatch.ui.mainActivity.home.videos.watchVideo.WatchVideoViewModel
import solutus.coronawatch.ui.mainActivity.info.camera.CameraInfoViewModel
import solutus.coronawatch.ui.mainActivity.info.photo.PhotoInfoViewModel
import solutus.coronawatch.ui.mainActivity.info.video.VideoInfoViewModel


import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.data.reposetory.implementation.ReportRepository


class CoronaWatchApplication : Application() , KodeinAware {

    override val kodein = Kodein.lazy{

        import(androidXModule(this@CoronaWatchApplication))
        bind() from singleton { NetworkConnexion(instance()) }
        bind() from singleton { ReportRepository(instance()) }
        bind() from singleton { ReportApi() }
        bind() from singleton { RobotApi() }
        bind<UserApi>() with singleton { UserApi.invoke() }
        bind<ContentApi>() with singleton { ContentApi.invoke() }
        bind<PhotoInfoViewModel>() with provider { PhotoInfoViewModel(instance()) }
        bind<VideoInfoViewModel>() with singleton  { VideoInfoViewModel(instance()) }
        bind<CameraInfoViewModel>() with singleton  { CameraInfoViewModel(instance()) }

        bind() from singleton { ContentRepository(instance()) }
        bind<WatchVideoViewModel>() with singleton { WatchVideoViewModel(instance()) }

        bind <ArticlesRepository>() with singleton { ArticlesRepository(instance(), instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind<NoveltiesViewModel>() with singleton { NoveltiesViewModel(instance()) }
        bind() from singleton { VideosYoutubeRepository(instance()) }
        bind() from  singleton { ListVideosYoutubeViewModel(instance()) }

    }




}
