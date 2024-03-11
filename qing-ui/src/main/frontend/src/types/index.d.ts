interface PromiseFn<T = any, R = T> {
  (...arg: T[]): Promise<R>;
}

interface ComponentElRef<T extends HTMLElement = HTMLDivElement> {
  $el: T;
}

function parseInt(s: string | number, radix?: number): number;

function parseFloat(string: string | number): number;
