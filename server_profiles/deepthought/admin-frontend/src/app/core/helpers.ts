
export const propPath = (obj: any, path: string) => {
    if (typeof path !== 'string' || !(obj && path)) {
        return undefined;
    }

    const paths = path.split('.');

    let current = obj;
    let found;

    for (const prop of paths) {
        if (current[prop] === undefined) {
            found = false;
        } else {
            found = true;
            current = current[prop];
        }
    }

    return found ? current : undefined;
};
