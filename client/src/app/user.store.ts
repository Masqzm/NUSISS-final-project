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
        providerId: '',
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
        this.loadUserFromLocalStorage()
    }

    private loadUserFromLocalStorage() {
        const storedUser = localStorage.getItem('user')
        if(storedUser) {
            this.setState({ 
                user: JSON.parse(storedUser)
            })
        }
    }

    resetStore() {
        console.info('logging out... resetting store')
        this.setState(INIT)
        localStorage.removeItem('user')
    }

    // Mutators - update mtds
    // setUser(newUser)                         // sets current user to store so user
    readonly setUser = this.updater<User>(      // can access pages requiring auth
        (slice: UserSlice, newUser: User) => {
            console.info('setting current user to store and storage: ', newUser)
            // Save the user to localStorage
            localStorage.setItem('user', JSON.stringify(newUser));

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