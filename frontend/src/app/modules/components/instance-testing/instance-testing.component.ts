import { Component } from '@angular/core';
import { InstanceTestingService } from '../../services/instance-testing.service';

@Component({
    selector: 'app-instance-testing',
    templateUrl: './instance-testing.component.html',
    styleUrls: ['./instance-testing.component.scss']
})
export class InstanceTestingComponent {
    publicIp!: string;
    privateIp!: string;

    constructor(private instanceTestingService: InstanceTestingService) {}

    getPublicIp(): void {
        this.instanceTestingService.getPublicIp().subscribe((ip) => {
            this.publicIp = ip;
        });
    }

    getPrivateIp(): void {
        this.instanceTestingService.getPrivateIp().subscribe((ip) => {
            this.privateIp = ip;
        });
    }
}
