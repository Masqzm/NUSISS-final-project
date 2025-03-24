import { inject, Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store"
import { User } from "./models";
import { AuthService } from "./services/auth.service";

// REF :
// https://github.com/Masqzm/NUSISS-csf-d38/blob/main/src/app/task.store.ts

interface UserSlice {
    user: User
    rsvpIds: []
}

const INIT: UserSlice = {
    user: {
        user_id: '',
        email: '',
        username: '',
        token: ''
    },
    rsvpIds: []
}

@Injectable()
export class UserStore extends ComponentStore<UserSlice> {
    //private userSvc = inject(UserService)
    //private rsvpSvc = inject()

    constructor() {
        super(INIT)
    }

    resetStore() {
        this.setState(INIT)
    }

    // Mutators - update mtds
    // setUser(newUser)                         // sets current user to store
    readonly setUser = this.updater<User>(      // updater<obj to be passed in to update>
        (slice: UserSlice, newUser: User) => {
            console.info('setting current user to store: ', newUser)
            return {
                user: newUser,
                rsvpIds: [...slice.rsvpIds]
            } as UserSlice
        }
    )

    // Selectors (query)
    readonly getUser = this.select<User>(
        (slice: UserSlice) => slice.user
    )
    readonly getUserToken = this.select<string>(
        (slice: UserSlice) => slice.user.token
    )
}