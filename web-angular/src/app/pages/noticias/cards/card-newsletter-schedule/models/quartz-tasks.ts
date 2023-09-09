export interface QuartzTasks {
    id?: number;
    createdAt?: Date;
    startAt: Date;
    recurrent: boolean,
    dayRange: number;
    endAt: Date;
}
