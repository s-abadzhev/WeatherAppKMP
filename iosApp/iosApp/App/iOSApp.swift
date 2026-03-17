import SwiftUI

@main
struct iOSApp: App {

    @State private var coordinator = AppCoordinator()

    var body: some Scene {
        WindowGroup {
            RootView()
                .environment(coordinator)
        }
    }
}
