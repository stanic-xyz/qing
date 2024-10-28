import type {PlayListDetail} from "@/apis/playList/types";

export interface Anime {
    id: number;
    animeId: string;
    name: string;
    cover: string;
    premiereDate: string;
    districtId: number;
    instruction: string;
    districtName: string;
    typeId: Number;
    typeName: string;
    originalName: string;
    otherName: string;
    author: string;
    companyId: number;
    companyName: string;
    playStatus: string;
    plotType: string;
    tagIds: number[];
    officialWebsite: string;
    playHeat: string;
}

export interface AnimeDetail extends Anime {
    playLists: PlayListDetail[];
}
