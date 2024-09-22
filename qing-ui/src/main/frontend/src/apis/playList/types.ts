import type {Episode} from "@/apis/episode/types";

export interface PlayList {
    id: number;
    name: string;
    icon: string;
    description: string;
    remark: string;
}

export interface PlayListDetail extends PlayList {
    episodeList: Episode[];
}
