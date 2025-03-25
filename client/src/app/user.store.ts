import { inject, Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store"
import { User } from "./models";
import { AuthService } from "./services/auth.service";

interface UserSlice {
    user: User
    //sessionExpiry: number
}

const INIT: UserSlice = {
    user: {
        userId: '',
        email: '',
        username: '',
        rsvpIds: []
    }
}

@Injectable()
export class UserStore extends ComponentStore<UserSlice> {
    //private userSvc = inject(UserService)
    //private rsvpSvc = inject()

    constructor() {
        super(INIT)
    }

    resetStore() {
        console.info('logging out... resetting store')
        this.setState(INIT)
    }

    // Mutators - update mtds
    // setUser(newUser)                         // sets current user to store so user
    readonly setUser = this.updater<User>(      // can access pages requiring auth
        (slice: UserSlice, newUser: User) => {
            console.info('setting current user to store: ', newUser)
            return {
                user: newUser
            } as UserSlice
        }
    )

    // Selectors (query)
    readonly getUser$ = this.select<User>(
        (slice: UserSlice) => slice.user
    )
}