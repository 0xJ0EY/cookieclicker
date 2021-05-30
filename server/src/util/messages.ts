export interface NetworkMessage {
    objectType: string;
    object: any;
}

export const encodeMessage = (message: NetworkMessage): string => {
    const payload = JSON.stringify(message.object);
    const messageFormatted = `${message.objectType}:${payload}`;
    
    return Buffer.from(messageFormatted, 'utf-8').toString('base64');
}

export const decodeMessage = (encodedMessage: string): NetworkMessage => {
    const message = Buffer.from(encodedMessage, 'base64').toString('utf-8');

    const index = message.indexOf(":");
    const objectType = message.substring(0, index);
    const objectJson = message.substring(index + 1);

    return {
        objectType: objectType,
        object: JSON.parse(objectJson)
    };
}