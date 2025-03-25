import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { UserStore } from "./user.store";
import { firstValueFrom } from "rxjs";
import { User } from "./models";

export const allowRegistration: CanActivateFn = 
    async (activatedRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
        const router = inject(Router)
        const userStore = inject(UserStore)
        const user: User = await firstValueFrom(userStore.getUser$())  

        //console.info(`userId: [${user.userId}]`)

        // Allow registration if user is NOT logged in
        if(user.userId == '')
            return true
        
        return router.parseUrl('/')     // route back to homepage
    }